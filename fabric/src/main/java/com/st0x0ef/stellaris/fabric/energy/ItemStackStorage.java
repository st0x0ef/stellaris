package com.st0x0ef.stellaris.fabric.energy;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.impl.transfer.context.SingleSlotContainerItemContext;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("UnstableApiUsage")
public class ItemStackStorage extends SingleStackStorage {
    private ItemStack stack;

    public ItemStackStorage(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    protected ItemStack getStack() {
        return stack;
    }

    @Override
    protected void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public static ContainerItemContext of(ItemStack stack) {
        return new SingleSlotContainerItemContext(new ItemStackStorage(stack));
    }
}
