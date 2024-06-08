package com.st0x0ef.stellaris.platform.systems.energy;

import com.st0x0ef.stellaris.common.systems.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.systems.item.ItemStackHolder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class EnergyContainerHelper {
    @ExpectPlatform
    public static EnergyContainer of(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static EnergyContainer of(ItemStackHolder holder) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static boolean holdsEnergy(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static boolean holdsEnergy(ItemStack stack) {
        throw new NotImplementedException();
    }
}
