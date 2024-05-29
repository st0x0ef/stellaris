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
    @Nullable T find(Level var1, BlockPos var2, @Nullable BlockState var3, @Nullable BlockEntity var4, @Nullable C var5);

    default @Nullable T find(BlockEntity block, @Nullable C direction) {
        return this.find(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    default @Nullable T find(Level level, BlockPos pos, @Nullable C direction) {
        return this.find(level, pos, null, null, direction);
    }

    void registerBlocks(BlockGetter<T, C> var1, Supplier<Block>... var2);

    void registerBlockEntities(BlockGetter<T, C> var1, Supplier<BlockEntityType<?>>... var2);

    public interface BlockGetter<T, C> {
        @Nullable T getContainer(Level var1, BlockPos var2, BlockState var3, @Nullable BlockEntity var4, @Nullable C var5);
    }
}
