package com.st0x0ef.stellaris.common.systems.item;

import net.minecraft.world.item.ItemStack;

/**
 * A wrapper for {@link ItemStack} that allows for dirty checking.
 * <p>
 * On Fabric, the stack is read-only. So we need to wrap it in order to track changes.
 * On all platforms, we need to track changes to the stack since the item may change, with buckets for example.
 * <p>
 * Create the holder before using transfer functions, and check {@link #isDirty()} after using them, no matter the platform.
 */
public class ItemStackHolder {
    private ItemStack stack;
    private boolean isDirty;

    public ItemStackHolder(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        if (!ItemStack.matches(stack, this.stack)) {
            this.stack = stack;
            isDirty = true;
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public ItemStackHolder copy() {
        return new ItemStackHolder(stack.copy());
    }
}
