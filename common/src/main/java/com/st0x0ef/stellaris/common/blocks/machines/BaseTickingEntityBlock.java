package com.st0x0ef.stellaris.common.blocks.machines;

import com.st0x0ef.stellaris.common.blocks.entities.machines.TickingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class BaseTickingEntityBlock extends BaseEntityBlock {

    protected BaseTickingEntityBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (hasTicker(level)) {
            return createTickerHelper(blockEntityType, getBlockEntityType(), (level1, blockPos, blockState, blockEntity) -> {
                ((TickingBlockEntity) blockEntity).tick();
            });
        }
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntityType().create(pos, state);
    }

    public abstract BlockEntityType<?> getBlockEntityType();

    public abstract boolean hasTicker(Level level);
}
