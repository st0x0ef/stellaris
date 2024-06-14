package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.items.oxygen.OxygenContainerItem;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidBucketHooks;
import net.minecraft.world.Container;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidTankHelper {

    public static final long BUCKET_AMOUNT = 1000;

    public static <T extends BlockEntity & Container> void extractFluidToItem(T blockEntity, FluidTank tank, int slot) {
        ItemStack inputStack = blockEntity.getItem(slot);
        if (!inputStack.isEmpty()) {
            if (!tank.isEmpty() && tank.getAmount() >= BUCKET_AMOUNT) {
                ItemStack resultStack;
                if (tank.getStack().getFluid().isSame(FluidRegistry.OXYGEN_STILL.get()) && inputStack.getItem() instanceof OxygenContainerItem) {
                    resultStack = inputStack.copy(); // TODO modify oxygen amount
                }
                else {
                    resultStack = new ItemStack(tank.getStack().getFluid().getBucket());
                }

                if (!resultStack.isEmpty()) {
                    blockEntity.setItem(slot, resultStack);
                    tank.grow(-BUCKET_AMOUNT);
                    blockEntity.setChanged();
                }
            }
        }
    }

    public static <T extends BlockEntity & Container> void extractFluidToItem(T blockEntity, FluidTank tank, int inputSlot, int outputSlot) {
        ItemStack inputStack = blockEntity.getItem(inputSlot);
        ItemStack outputStack = blockEntity.getItem(outputSlot);
        boolean hasSpace = outputStack.getCount() < outputStack.getMaxStackSize();

        if (!inputStack.isEmpty() && (outputStack.isEmpty() || hasSpace)) {
            if (!tank.isEmpty() && tank.getAmount() >= BUCKET_AMOUNT) {
                if (inputStack.getItem() instanceof BucketItem item && FluidBucketHooks.getFluid(item).isSame(Fluids.EMPTY)) {
                    ItemStack resultStack = new ItemStack(tank.getStack().getFluid().getBucket());
                    boolean success = false;

                    if (outputStack.isEmpty()) {
                        blockEntity.setItem(outputSlot, resultStack);
                        success = true;
                    }
                    else if (ItemStack.isSameItem(outputStack, resultStack) && hasSpace) {
                        outputStack.grow(1);
                        success = true;
                    }

                    if (success) {
                        inputStack.shrink(1);
                        tank.grow(-BUCKET_AMOUNT);
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
                        else return false;

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
}
