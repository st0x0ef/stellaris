package com.st0x0ef.stellaris.common.blocks.entities;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ImplementedInventory extends WorldlyContainer {

    NonNullList<ItemStack> getItems();

    void loadAdditional(CompoundTag compoundTag);

    @Override
    default int @NotNull [] getSlotsForFace(Direction direction) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    @Override
    default boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return true;
    }

    @Override
    default boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    default int getContainerSize() {
        return getItems().size();
    }

    default boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    default ItemStack getItem(int i) {
        return getItems().get(i);
    }

    default ItemStack removeItem(int i, int j) {
        ItemStack result = ContainerHelper.removeItem(getItems(), i, j);
        if (!result.isEmpty()) {
            setChanged();
        }

        return result;
    }

    default ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(getItems(), i);
    }

    default void setItem(int i, ItemStack itemStack) {
        getItems().set(i, itemStack);
        if (itemStack.getCount() > getMaxStackSize()) {
            itemStack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    default void setChanged() {

    }

    default boolean stillValid(Player player) {
        return true;
    }

    default void clearContent() {
        getItems().clear();

    }
}
