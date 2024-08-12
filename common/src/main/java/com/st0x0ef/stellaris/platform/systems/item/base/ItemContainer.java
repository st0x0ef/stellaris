package com.st0x0ef.stellaris.platform.systems.item.base;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemContainer extends Clearable {
    @ExpectPlatform
    static ItemContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException();
    }

    static ItemContainer of(Level level, BlockPos pos, @Nullable Direction direction) {
        return ItemContainer.of(level, pos, null, null, direction);
    }

    static ItemContainer of(BlockEntity block, @Nullable Direction direction) {
        return ItemContainer.of(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    int getSlots();

    @NotNull
    ItemStack getStackInSlot(int slot);

    int getSlotLimit(int slot);

    boolean isItemValid(int slot, @NotNull ItemStack stack);

    @NotNull
    ItemStack insertItem(@NotNull ItemStack stack, boolean simulate);

    @NotNull
    ItemStack insertIntoSlot(int slot, @NotNull ItemStack stack, boolean simulate);

    @NotNull
    ItemStack extractItem(int amount, boolean simulate);

    @NotNull
    ItemStack extractFromSlot(int slot, int amount, boolean simulate);

    boolean isEmpty();
}
