package com.st0x0ef.stellaris.common.energy;

import net.minecraft.world.item.ItemStack;

public class ItemStackHolder {
    private ItemStack stack;
    private boolean isDirty;

    public ItemStackHolder(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack stack) {
        if (!ItemStack.matches(stack, this.stack)) {
            this.stack = stack;
            this.isDirty = true;
        }

    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public ItemStackHolder copy() {
        return new ItemStackHolder(this.stack.copy());
    }
}
