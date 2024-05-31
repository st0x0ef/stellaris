package com.st0x0ef.stellaris.common.systems.item.utils;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.item.base.ItemContainerExtras;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotItemContainer<T extends ItemContainer & ItemContainerExtras> extends Slot {
    private static final Container EMPTY = new SimpleContainer(0);
    private final T container;

    public SlotItemContainer(T container, int slot, int x, int y) {
        super(EMPTY, slot, x, y);
        this.container = container;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (stack.isEmpty())
            return false;
        return container.isItemValid(index, stack);
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        return container.getStackInSlot(index);
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        container.setStackInSlot(index, stack);
        this.setChanged();
    }

    @Override
    public void onQuickCraft(@NotNull ItemStack oldStackIn, @NotNull ItemStack newStackIn) {
    }

    @Override
    public int getMaxStackSize() {
        return container.getSlotLimit(this.index);
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        int maxInput = stack.getMaxStackSize();
        ItemStack maxAdd = stack.copyWithCount(maxInput);
        ItemStack currentStack = container.getStackInSlot(index);
        container.setStackInSlot(index, ItemStack.EMPTY);
        ItemStack inserted = container.insertIntoSlot(index, maxAdd, true);
        container.setStackInSlot(index, currentStack);
        return inserted.getCount();
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !container.extractFromSlot(index, 1, true).isEmpty();
    }

    @Override
    @NotNull
    public ItemStack remove(int amount) {
        return container.extractFromSlot(index, amount, false);
    }
}
