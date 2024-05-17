package com.st0x0ef.stellaris.common.blocks;

import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OxygenBlock extends BaseEntityBlock {
    protected OxygenBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof OxygenBlockEntity entity) {
            entity.tick();
        }
    }
}
