package com.st0x0ef.stellaris.platform.systems.energy.fabric;

import com.st0x0ef.stellaris.common.systems.item.ItemStackHolder;
import com.st0x0ef.stellaris.fabric.systems.ItemStackStorage;
import com.st0x0ef.stellaris.fabric.systems.energy.PlatformEnergyManager;
import com.st0x0ef.stellaris.fabric.systems.energy.PlatformItemEnergyManager;
import com.st0x0ef.stellaris.platform.systems.energy.EnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("unused")
public class EnergyContainerHelperImpl {
    public static EnergyContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformEnergyManager.of(level, pos, state, entity, direction);
    }

    public static EnergyContainer of(ItemStackHolder holder) {
        return PlatformItemEnergyManager.of(holder);
    }

    public static boolean holdsEnergy(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return EnergyStorage.SIDED.find(level, pos, state, entity, direction) != null;
    }

    public static boolean holdsEnergy(ItemStack stack) {
        return EnergyStorage.ITEM.find(stack, ItemStackStorage.of(stack)) != null;
    }
}
