package com.st0x0ef.stellaris.common.energy.base;

import com.st0x0ef.stellaris.common.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.energy.util.Updatable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * A functional interface representing an energy block in a Botarium.
 *
 * @param <T> the type of the energy storage object. Botarium provides a default implementation for this with {@link WrappedBlockEnergyContainer}.
 */
@FunctionalInterface
public interface EnergyBlock<T extends EnergyContainer & Updatable> {

    /**
     * Retrieves the energy storage object for a given level, position, state, entity, and direction.
     *
     * @param level     the level
     * @param pos       the block position
     * @param state     the block state
     * @param entity    the block entity (may be null)
     * @param direction the direction (may be null)
     * @return the energy storage object if available, or null if not found
     */
    T getEnergyStorage(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction);
}
