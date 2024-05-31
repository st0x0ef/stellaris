package com.st0x0ef.stellaris.common.energy.generic.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface BlockContainerLookup<T, C> {

    /**
     * @return The {@link T} for the block.
     */
    @Nullable
    T find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable C direction);

    @Nullable
    default T find(BlockEntity block, @Nullable C direction) {
        return find(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    @Nullable
    default T find(Level level, BlockPos pos, @Nullable C direction) {
        return find(level, pos, null, null, direction);
    }

    void registerBlocks(BlockGetter<T, C> getter, Supplier<Block>... containers);

    void registerBlockEntities(BlockGetter<T, C> getter, Supplier<BlockEntityType<?>>... containers);

    interface BlockGetter<T, C> {
        @Nullable
        T getContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable C direction);
    }
}
