package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.utils.OxygenUtils;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidBucketHooks;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.platform.Platform;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidTankHelper {

    public static final long BUCKET_AMOUNT = FluidStackHooks.bucketAmount();
    public static final long OXYGEN_TANK_FILL_AMOUNT = Platform.isFabric() ? 810 : 10;

    public static <T extends BlockEntity & Container> void extractFluidToItem(T blockEntity, FluidTank tank, int slot) {
        ItemStack inputStack = blockEntity.getItem(slot);
        if (!inputStack.isEmpty()) {
            if (!tank.isEmpty()) {
                boolean isTank = inputStack.has(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get());

                if (tank.getAmount() >= BUCKET_AMOUNT || (isTank && tank.getAmount() >= OXYGEN_TANK_FILL_AMOUNT)) {
                    ItemStack resultStack = ItemStack.EMPTY;

                    if (isTank && tank.getStack().getFluid().isSame(FluidRegistry.OXYGEN_STILL.get())) {
                        resultStack = inputStack.copy();
                        long storedOxygen = inputStack.get(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get()).oxygen();

                        if (storedOxygen + OXYGEN_TANK_FILL_AMOUNT >= inputStack.get(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get()).capacity()) {
                            return;
                        }

                        OxygenUtils.addOxygen(resultStack, OXYGEN_TANK_FILL_AMOUNT);
                        tank.shrink(OXYGEN_TANK_FILL_AMOUNT);
                    }
                    else if (!isTank && isEmptyBucket(inputStack.getItem())) {
                        ItemStack stack = new ItemStack(tank.getStack().getFluid().getBucket());
                        if (!stack.isEmpty() && !isEmptyBucket(stack.getItem())) {
                            resultStack = stack;
                            tank.shrink(BUCKET_AMOUNT);
                        }
                    }

                    if (!resultStack.isEmpty()) {
                        blockEntity.setItem(slot, resultStack);
                        blockEntity.setChanged();
                    }
                }
            }
        }
    }

    public static boolean isEmptyBucket(Item item) {
        return item instanceof BucketItem bucketItem && FluidBucketHooks.getFluid(bucketItem).isSame(Fluids.EMPTY);
    }

    public static <T extends BlockEntity & Container> void extractFluidToItem(T blockEntity, FluidTank tank, int inputSlot, int outputSlot) {
        ItemStack outputStack = blockEntity.getItem(outputSlot);
        ItemStack inputStack = blockEntity.getItem(inputSlot);
        boolean hasSpace = outputStack.getCount() < outputStack.getMaxStackSize();

        if (!inputStack.isEmpty() && (outputStack.isEmpty() || hasSpace)) {
            boolean canFuel = inputStack.has(DataComponentsRegistry.STORED_FUEL_COMPONENT.get());

            if (!tank.isEmpty() && (tank.getAmount() >= BUCKET_AMOUNT || canFuel)) {
                ItemStack resultStack = ItemStack.EMPTY;

                if (isEmptyBucket(inputStack.getItem())) {
                    resultStack = new ItemStack(tank.getStack().getFluid().getBucket());
                }
                else if (canFuel) {
                    resultStack = inputStack.copy();
                }

                if (!resultStack.isEmpty()) {
                    boolean success = false;
                    long amount = BUCKET_AMOUNT;

                    if (outputStack.isEmpty()) {
                        blockEntity.setItem(outputSlot, resultStack);
                        success = true;
                    }
                    else if (ItemStack.isSameItem(outputStack, resultStack) && hasSpace) {
                        outputStack.grow(1);
                        success = true;
                    }

                    if (success) {
                        if (canFuel) {
                            long fuel = inputStack.getOrDefault(DataComponentsRegistry.STORED_FUEL_COMPONENT.get(), 0).longValue();
                            amount = Math.min(JetSuit.MAX_FUEL_CAPACITY - fuel, tank.getAmount());
                            resultStack.set(DataComponentsRegistry.STORED_FUEL_COMPONENT.get(), Mth.clamp(fuel + amount, 0, JetSuit.MAX_FUEL_CAPACITY));
                        }

                        inputStack.shrink(1);
                        tank.shrink(amount);
                        blockEntity.setChanged();
                    }
                }
            }
        }
    }

    public static void addToTank(FluidTank tank, FluidStack stack) {
        FluidStack tankStack = tank.getStack();
        if (tankStack.isEmpty()) {
            tank.setFluid(stack.getFluid(), stack.getAmount());
        }
        else if (tank.getStack().isFluidEqual(stack)) {
            tank.grow(stack.getAmount());
        }
    }

    public static <T extends BlockEntity & Container> boolean addFluidFromBucket(T blockEntity, FluidTank tank, int inputSlot, int outputSlot) {
        if (tank.getAmount() + BUCKET_AMOUNT < tank.getMaxCapacity()) {
            ItemStack inputStack = blockEntity.getItem(inputSlot);
            ItemStack outputStack = blockEntity.getItem(outputSlot);
            boolean hasSpace = outputStack.getCount() < outputStack.getMaxStackSize();

            if (!inputStack.isEmpty() && (outputStack.isEmpty() || hasSpace)) {
                if (inputStack.getItem() instanceof BucketItem item) {
                    Fluid fluid = FluidBucketHooks.getFluid(item);

                    if ((!tank.isEmpty() && tank.getStack().getFluid() == fluid) || (tank.isEmpty() && !fluid.isSame(Fluids.EMPTY))) {
                        if (outputStack.isEmpty()) {
                            blockEntity.setItem(outputSlot, new ItemStack(Items.BUCKET));
                        }
                        else if (outputStack.is(Items.BUCKET) && hasSpace) {
                            outputStack.grow(1);
                        }
                        else {
                            return false;
                        }

                        blockEntity.setItem(inputSlot, ItemStack.EMPTY);
                        addToTank(tank, FluidStack.create(fluid, BUCKET_AMOUNT));
                        blockEntity.setChanged();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static long convertFromMb(long amount) {
        return amount * 81;
    }

    public static long convertToMb(long amount) {
        return amount / 81;
    }
}
