package com.st0x0ef.stellaris.common.systems.fluid.base;

import com.st0x0ef.stellaris.common.systems.fluid.impl.WrappedBlockFluidContainer;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * This interface represents a block that contains a fluid container.
 *
 * @param <T> The type of the fluid container. Must implement the {@link FluidContainer} and {@link Updatable} interfaces. Botarium provides a default implementation for this with {@link WrappedBlockFluidContainer}.
 */
public interface FluidBlock<T extends FluidContainer & Updatable> {

    /**
     * @return The {@link ItemFluidContainer} for the block.
     */
    @Nullable
    T getFluidContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction);
}