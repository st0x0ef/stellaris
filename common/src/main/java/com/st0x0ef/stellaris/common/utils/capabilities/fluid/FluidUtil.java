package com.st0x0ef.stellaris.common.utils.capabilities.fluid;

import com.fej1fun.potentials.capabilities.Capabilities;
import com.fej1fun.potentials.fluid.BaseFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidItemStorage;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("all")
public class FluidUtil {
    public static void moveFluidToItem(int tank, UniversalFluidStorage from, int slot, NonNullList<ItemStack> items, long amount) {
        if (items.get(slot).isEmpty()) return;
        UniversalFluidItemStorage to = Capabilities.Fluid.ITEM.getCapability(items.get(slot));
        if (to == null) return;
        amount = Math.min(amount, to.getTankCapacity(0) - to.getFluidInTank(0).getAmount());
        moveFluid(from, to, from.getFluidInTank(tank).copyWithAmount(amount));
        items.set(slot, to.getContainer());
    }

    public static void moveFluidFromItem(int tank, int slot, NonNullList<ItemStack> items, UniversalFluidStorage to, long amount) {
        if (items.get(slot).isEmpty()) return;
        UniversalFluidItemStorage from = Capabilities.Fluid.ITEM.getCapability(items.get(slot));
        if (from == null) return;
        amount = Math.min(amount, to.getTankCapacity(tank) - to.getFluidInTank(tank).getAmount());
        moveFluid(from, to, from.getFluidInTank(tank).copyWithAmount(amount));
        items.set(slot, from.getContainer());
    }

    public static FluidStack moveFluid(UniversalFluidStorage from, UniversalFluidStorage to, FluidStack stack) {
        if (stack.isEmpty()) return FluidStack.empty();

        FluidStack inserted = FluidStack.create(stack, to.fill(from.drain(stack, true), true));

        if (inserted.isEmpty()) return FluidStack.empty();

        from.drain(inserted.copy(), false);
        to.fill(inserted.copy(), false);

        return inserted;
    }

    /// ignores max fill/drain limits
    public static FluidStack moveFluidWithSet(BaseFluidStorage from, BaseFluidStorage to, FluidStack stack) {
        FluidStack inserted = FluidStack.create(stack ,to.fill(from.drain(stack, true), true));

        if (inserted.isEmpty()) return FluidStack.empty();

        from.drainWithoutLimits(inserted, false);
        to.fillWithoutLimits(inserted, false);
        return inserted;
    }

    public static void distributeFluidNearby(Level level, BlockPos pos, FluidStack stack) {
        distributeFluidNearby(level, pos, stack, null);
    }

    public static void distributeFluidNearby(Level level, BlockPos pos, FluidStack stack, List<Direction> outputDirections) {
        if (outputDirections == null || outputDirections.isEmpty()) {
            distributeInAllDirections(level, pos, stack);
            return;
        }
        distributeInDirections(level, pos, stack, outputDirections);
    }

    private static long distributeInDirections(Level level, BlockPos pos, FluidStack stack, List<Direction> outputDirections) {
        Map<UniversalFluidStorage, UniversalFluidStorage> pairs = new HashMap<>();
        for (Direction direction : outputDirections) {
            UniversalFluidStorage from = Capabilities.Fluid.BLOCK.getCapability(level, pos, direction);
            if (from == null) {
                continue;
            }

            FluidStack drained = from.drain(stack, true);
            if (drained.getAmount() <= 0) {
                continue;
            }

            UniversalFluidStorage to = Capabilities.Fluid.BLOCK.getCapability(level, pos.relative(direction), direction.getOpposite());
            if (to == null) {
                continue;
            }

            if (to.fill(drained, true) <= 0) {
                continue;
            }

            pairs.put(from, to);
        }

        AtomicLong toDistribute = new AtomicLong(stack.getAmount());
        AtomicLong receivers = new AtomicLong(pairs.size());
        pairs.forEach((energyFrom, energyTo) -> {
            toDistribute.addAndGet(-moveFluid(energyFrom, energyTo, stack.copyWithAmount(toDistribute.get() / receivers.get())).getAmount());
            receivers.getAndDecrement();
        });
        return stack.getAmount() - toDistribute.get();
    }

    private static void distributeInAllDirections(Level level, BlockPos pos, FluidStack stack) {
        UniversalFluidStorage from = Capabilities.Fluid.BLOCK.getCapability(level, pos, null);
        if (from == null) {
            return;
        }

        long amount = from.drain(stack, true).getAmount();

        if (amount == 0L) {
            return;
        }

        FluidStack finalStack = stack.copyWithAmount(amount);

        List<UniversalFluidStorage> toSend = Direction.stream()
                .map(direction -> Capabilities.Fluid.BLOCK.getCapability(level, pos.relative(direction), direction.getOpposite()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(fluidStorage -> fluidStorage.fill(finalStack, true)))
                .filter(fluidStorage -> {
                    for (int i = 0; i < fluidStorage.getTanks(); i++) {
                        fluidStorage.isFluidValid(i, finalStack);
                        return true;
                    }
                    return false;
                })
                .toList();

        if (toSend.isEmpty()) {
            return;
        }

        int receivers = toSend.size();
        long toDistribute = finalStack.getAmount();

        for (UniversalFluidStorage to : toSend) {
            toDistribute -= moveFluid(from, to, stack.copyWithAmount(finalStack.getAmount() / receivers)).getAmount();
        }
    }
}
