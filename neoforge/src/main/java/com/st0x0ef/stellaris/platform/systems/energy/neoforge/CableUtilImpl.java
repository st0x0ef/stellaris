package com.st0x0ef.stellaris.platform.systems.energy.neoforge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class CableUtilImpl {
    public static boolean isEnergyContainer(BlockEntity blockEntity, Direction direction) {
        if (blockEntity==null) return false;

        Level level = blockEntity.getLevel();
        BlockPos blockPos = blockEntity.getBlockPos();
        BlockState blockState = blockEntity.getBlockState();

        IEnergyStorage energyStorage = (IEnergyStorage)level.getCapability(Capabilities.EnergyStorage.BLOCK, blockPos, blockState, blockEntity, null);
        return energyStorage!=null;
    }
}
