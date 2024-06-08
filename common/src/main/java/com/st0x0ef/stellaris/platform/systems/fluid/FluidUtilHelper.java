package com.st0x0ef.stellaris.platform.systems.fluid;

import com.st0x0ef.stellaris.common.systems.fluid.base.FluidContainer;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;
import com.st0x0ef.stellaris.common.systems.fluid.base.ItemFluidContainer;
import com.st0x0ef.stellaris.common.systems.item.ItemStackHolder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class FluidUtilHelper {

    //FluidContainer Util
    @ExpectPlatform
    public static FluidContainer of(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static ItemFluidContainer of(ItemStackHolder holder) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static boolean holdsFluid(ItemStack stack) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static boolean holdsFluid(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException();
    }


    //FluidHolder Util
    @ExpectPlatform
    public static FluidHolder of(Fluid fluid, long amount, @Nullable DataComponentPatch dataComponentPatch) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static FluidHolder empty() {
        throw new NotImplementedException();
    }

}