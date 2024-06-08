package com.st0x0ef.stellaris.platform.systems.fluid.fabric;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.fabric.ItemStackStorage;
import earth.terrarium.botarium.fabric.fluid.storage.PlatformFluidContainer;
import earth.terrarium.botarium.fabric.fluid.storage.PlatformFluidItemHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidContainer.class)
@SuppressWarnings("unused")
public interface FluidContainerImpl {

    @Nullable
    @ImplementedByExtension
    static FluidContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformFluidContainer.of(level, pos, state, entity, direction);
    }

    @Nullable
    @ImplementedByExtension
    static ItemFluidContainer of(ItemStackHolder holder) {
        return PlatformFluidItemHandler.of(holder);
    }

    @ImplementedByExtension
    static boolean holdsFluid(ItemStack stack) {
        return FluidStorage.ITEM.find(stack, ItemStackStorage.of(stack)) != null;
    }

    @ImplementedByExtension
    static boolean holdsFluid(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return FluidStorage.SIDED.find(level, pos, state, entity, direction) != null;
    }
}
