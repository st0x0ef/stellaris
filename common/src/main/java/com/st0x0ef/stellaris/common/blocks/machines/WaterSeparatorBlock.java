package com.st0x0ef.stellaris.common.blocks.machines;

import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WaterSeparatorBlock extends BaseMachineBlock {

    public WaterSeparatorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.WATER_SEPARATOR_ENTITY.get();
    }

    @Override
    public boolean hasTicker(Level level) {
        return !level.isClientSide;
    }
}
