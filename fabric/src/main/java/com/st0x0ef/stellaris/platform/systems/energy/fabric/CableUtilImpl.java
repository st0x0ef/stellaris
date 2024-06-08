package com.st0x0ef.stellaris.platform.systems.energy.fabric;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.api.EnergyStorage;

public class CableUtilImpl {
    public static boolean isEnergyContainer(BlockEntity blockEntity, Direction direction) {
        if (blockEntity==null) return false;

        EnergyStorage energyStorage = (EnergyStorage)EnergyStorage.SIDED.find(blockEntity.getLevel(), blockEntity.getBlockPos(), direction);
        return energyStorage != null;
    }
}
