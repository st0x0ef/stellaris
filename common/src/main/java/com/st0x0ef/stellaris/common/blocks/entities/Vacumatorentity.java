package com.st0x0ef.stellaris.common.blocks.entities;

import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class Vacumatorentity extends BlockEntity {


    public Vacumatorentity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(EntityRegistry.VACUMATOR.get(), pos, blockState);
    }




}


