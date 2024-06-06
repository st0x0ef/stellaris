package com.st0x0ef.stellaris.common.blocks.machines;

import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WaterSeparatorBlock extends BaseMachineBlock {

    public WaterSeparatorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, BlockEntityRegistry.WATER_SEPARATOR_ENTITY.get(), WaterSeparatorBlockEntity::serverTick);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WaterSeparatorBlockEntity(pos, state);
    }
}
