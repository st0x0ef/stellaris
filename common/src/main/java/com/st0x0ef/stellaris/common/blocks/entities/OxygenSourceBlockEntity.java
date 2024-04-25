package com.st0x0ef.stellaris.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OxygenSourceBlockEntity extends BlockEntity {
    protected OxygenSourceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int range) {
        super(type, pos, blockState);
    }
}
