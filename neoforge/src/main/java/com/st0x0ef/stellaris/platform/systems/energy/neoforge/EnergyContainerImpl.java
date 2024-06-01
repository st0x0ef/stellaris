package com.st0x0ef.stellaris.platform.systems.energy.neoforge;

import com.st0x0ef.stellaris.common.systems.item.ItemStackHolder;
import com.st0x0ef.stellaris.neoforge.systems.energy.PlatformEnergyManager;
import com.st0x0ef.stellaris.platform.systems.energy.EnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

public interface EnergyContainerImpl {
    static EnergyContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformEnergyManager.of(level, pos, state, entity, direction);
    }

    static EnergyContainer of(ItemStackHolder holder) {
        return PlatformEnergyManager.of(holder);
    }

    static boolean holdsEnergy(ItemStack stack) {
        return stack.getCapability(Capabilities.EnergyStorage.ITEM) != null;
    }

    static boolean holdsEnergy(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, entity, direction) != null;
    }
}
