package com.st0x0ef.stellaris.fabric.systems.item;

import com.st0x0ef.stellaris.common.systems.util.Updatable;
import com.st0x0ef.stellaris.platform.systems.item.base.ItemContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record UpdatingItemContainer(ItemContainer container) implements ItemContainer, Updatable {
    public static UpdatingItemContainer of(ItemContainer container) {
        return container == null ? null : new UpdatingItemContainer(container);
    }

    //public static ItemContainer of(ItemContainer container) {
    //    return null;
    //}

    @Override
    public int getSlots() {
        return container.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return container.getStackInSlot(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return container.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return container.isItemValid(slot, stack);
    }

    @Override
    public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
        ItemStack itemStack = container.insertItem(stack, simulate);
        if (!simulate) {
            update();
        }
        return itemStack;
    }

    @Override
    public @NotNull ItemStack insertIntoSlot(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack itemStack = container.insertIntoSlot(slot, stack, simulate);
        if (!simulate) {
            update();
        }
        return itemStack;
    }

    @Override
    public @NotNull ItemStack extractItem(int amount, boolean simulate) {
        ItemStack itemStack = container.extractItem(amount, simulate);
        if (!simulate) {
            update();
        }
        return itemStack;
    }

    @Override
    public @NotNull ItemStack extractFromSlot(int slot, int amount, boolean simulate) {
        ItemStack itemStack = container.extractFromSlot(slot, amount, simulate);
        if (!simulate) {
            update();
        }
        return itemStack;
    }

    @Override
    public boolean isEmpty() {
        return container.isEmpty();
    }

    @Override
    public void clearContent() {
        container.clearContent();
    }

    @Override
    public void update() {
        if (container instanceof Updatable updatable) {
            updatable.update();
        }
    }
}
