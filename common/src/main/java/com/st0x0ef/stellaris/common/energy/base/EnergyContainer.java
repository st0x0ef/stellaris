package com.st0x0ef.stellaris.common.energy.base;

import com.st0x0ef.stellaris.common.energy.ItemStackHolder;
import com.st0x0ef.stellaris.common.energy.util.Serializable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface EnergyContainer extends Serializable, Clearable {
    static EnergyContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformEnergyManager.of(level, pos, state, entity, direction);
    }

    static @Nullable EnergyContainer of(Level level, BlockPos pos, @Nullable Direction direction) {
        return of(level, pos, null, null, direction);
    }

    static @Nullable EnergyContainer of(BlockEntity block, @Nullable Direction direction) {
        return of(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    static EnergyContainer of(ItemStackHolder holder) {
        return PlatformItemEnergyManager.of(holder);
    }

    static boolean holdsEnergy(ItemStack stack) {
        return EnergyStorage.ITEM.find(stack, ItemStackStorage.of(stack)) != null;
    }

    static boolean holdsEnergy(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return EnergyStorage.SIDED.find(level, pos, state, entity, direction) != null;
    }

    static boolean holdsEnergy(Level level, BlockPos pos, @Nullable Direction direction) {
        return holdsEnergy(level, pos, null, null, direction);
    }

    static boolean holdsEnergy(BlockEntity block, @Nullable Direction direction) {
        return holdsEnergy(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    long insertEnergy(long var1, boolean var3);

    default long internalInsert(long amount, boolean simulate) {
        return this.insertEnergy(amount, simulate);
    }

    long extractEnergy(long var1, boolean var3);

    default long internalExtract(long amount, boolean simulate) {
        return this.extractEnergy(amount, simulate);
    }

    void setEnergy(long var1);

    long getStoredEnergy();

    long getMaxCapacity();

    long maxInsert();

    long maxExtract();

    boolean allowsInsertion();

    boolean allowsExtraction();

    EnergySnapshot createSnapshot();

    default void readSnapshot(EnergySnapshot snapshot) {
        snapshot.loadSnapshot(this);
    }
}
