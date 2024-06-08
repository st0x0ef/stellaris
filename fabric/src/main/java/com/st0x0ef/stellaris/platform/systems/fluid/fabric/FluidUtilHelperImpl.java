package com.st0x0ef.stellaris.platform.systems.fluid.fabric;

import com.st0x0ef.stellaris.common.systems.fluid.base.FluidContainer;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;
import com.st0x0ef.stellaris.common.systems.fluid.base.ItemFluidContainer;
import com.st0x0ef.stellaris.common.systems.item.ItemStackHolder;
import com.st0x0ef.stellaris.fabric.systems.ItemStackStorage;
import com.st0x0ef.stellaris.fabric.systems.fluid.holder.FabricFluidHolder;
import com.st0x0ef.stellaris.fabric.systems.fluid.storage.PlatformFluidContainer;
import com.st0x0ef.stellaris.fabric.systems.fluid.storage.PlatformFluidItemHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentPatch;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class FluidUtilHelperImpl {
    //FluidContainer Util
    @Nullable
    public static FluidContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformFluidContainer.of(level, pos, state, entity, direction);
    }

    @Nullable
    public static ItemFluidContainer of(ItemStackHolder holder) {
        return PlatformFluidItemHandler.of(holder);
    }

    public static boolean holdsFluid(ItemStack stack) {
        return FluidStorage.ITEM.find(stack, ItemStackStorage.of(stack)) != null;
    }

    public static boolean holdsFluid(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return FluidStorage.SIDED.find(level, pos, state, entity, direction) != null;
    }


    //FluidHolder Util
    public static FluidHolder of(Fluid fluid, long amount, DataComponentPatch dataComponentPatch) {
        return FabricFluidHolder.of(fluid, amount, dataComponentPatch);
    }

    public static FluidHolder empty() {
        return FabricFluidHolder.empty();
    }
}
