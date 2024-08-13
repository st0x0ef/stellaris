package com.st0x0ef.stellaris.neoforge.systems.item;

import com.st0x0ef.stellaris.platform.systems.item.base.ItemContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PlatformItemContainer(IItemHandler itemHandler) implements ItemContainer {
    public static PlatformItemContainer of(@Nullable IItemHandler itemHandler) {
        return itemHandler == null ? null : new PlatformItemContainer(itemHandler);
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return itemHandler.isItemValid(slot, stack);
    }

    @Override
    public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
        ItemStack leftover = stack.copy();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            leftover = itemHandler.insertItem(i, leftover, simulate);
            if (leftover.isEmpty()) {
                return stack.copy();
            }
        }
        return stack.copyWithCount(stack.getCount() - leftover.getCount());
    }

    @Override
    public @NotNull ItemStack insertIntoSlot(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack itemStack = itemHandler.insertItem(slot, stack, simulate);
        return itemStack.getCount() >= stack.getCount() ? ItemStack.EMPTY : stack.copyWithCount(stack.getCount() - itemStack.getCount());
    }

    @Override
    public @NotNull ItemStack extractItem(int amount, boolean simulate) {
        ItemStack extracted = ItemStack.EMPTY;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) continue;
            if (extracted.isEmpty()) {
                extracted = itemHandler.extractItem(i, amount, simulate);
            } else {
                if (ItemStack.isSameItemSameTags(extracted, itemHandler.getStackInSlot(i))) {
                    extracted.grow(itemHandler.extractItem(i, amount - extracted.getCount(), simulate).getCount());
                }
            }
            if (extracted.getCount() >= amount) {
                break;
            }
        }
        return extracted;
    }

    @Override
    public @NotNull ItemStack extractFromSlot(int slot, int amount, boolean simulate) {
        return itemHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.extractItem(i, itemHandler.getStackInSlot(i).getCount(), false);
        }
    }
}
