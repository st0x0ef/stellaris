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

    public static void distributeFluidNearby(Level level, BlockPos pos, long amount) {
        distributeFluidNearby(level, pos, amount, null);
    }

    public static void distributeFluidNearby(Level level, BlockPos pos, long amount, List<Direction> outputDirections) {
        if (outputDirections == null || outputDirections.isEmpty()) {
            distributeInAllDirections(level, pos, amount);
            return;
        }
        distributeInDirections(level, pos, amount, outputDirections);
    }

    private static long distributeInDirections(Level level, BlockPos pos, long amount, List<Direction> outputDirections) {
        Map<UniversalFluidStorage, UniversalFluidStorage> pairs = new HashMap<>();
        UniversalFluidStorage from;
        UniversalFluidStorage to;
        for (Direction direction : outputDirections) {
            from = Capabilities.Fluid.BLOCK.getCapability(level, pos, direction);
            if (from == null) {
                continue;
            }
            FluidStack drained = from.drain(amount, true);
            if (!(drained.getAmount() > 0)) {
                continue;
            }
            to = Capabilities.Fluid.BLOCK.getCapability(level, pos, direction);
            if (to == null) {
                continue;
            }
            if (!(to.fill(drained, true) > 0)) {
                continue;
            }
            pairs.put(from, to);
        }

        AtomicLong toDistribute = new AtomicLong(amount);
        AtomicLong receivers = new AtomicLong(pairs.size());
        pairs.forEach((energyFrom, energyTo) -> {
            toDistribute.addAndGet(-moveFluid(energyFrom, energyTo, toDistribute.get() / receivers.get()));
            receivers.getAndDecrement();
        });
        return amount - toDistribute.get();
    }

    private static void distributeInAllDirections(Level level, BlockPos pos, long amount) {
        UniversalFluidStorage from = Capabilities.Fluid.BLOCK.getCapability(level, pos, null);
        if (from == null) {
            return;
        }

        if (from.drain(amount, true).getAmount() == 0) {
            return;
        }

        List<UniversalFluidStorage> toSend = Direction.stream()
                .map(direction -> Capabilities.Fluid.BLOCK.getCapability(level, pos.relative(direction), direction.getOpposite()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(fluidStorage -> fluidStorage.fill(amount, true)))
                .filter(UniversalFluidStorage::isFluidValid)
                .toList();

        if (toSend.isEmpty()) {
            return;
        }

        int receivers = toSend.size();
        long toDistribute = amount;

        for (UniversalFluidStorage to : toSend) {
            toDistribute -= moveFluid(from, to, toDistribute / receivers);
            receivers--;
        }
    }

    public static long moveFluid(UniversalFluidStorage from, UniversalFluidStorage to, long amount) {
        long filled = to.fill(from.drain(amount, true), true);
        if (filled > 0) {
            to.fill(from.drain(filled, false), false);
            return filled;
        }
        return 0;
    }
}
