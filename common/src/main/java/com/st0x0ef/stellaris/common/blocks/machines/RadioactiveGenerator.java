package com.st0x0ef.stellaris.common.blocks.machines;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.blocks.entities.machines.RadioactiveGeneratorEntity;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RadioactiveGenerator extends BaseLitMachineBlock {

    public RadioactiveGenerator(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(RadioactiveGenerator::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RadioactiveGeneratorEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, BlockEntityRegistry.RADIOACTIVE_GENERATOR.get(), RadioactiveGeneratorEntity::serverTick);
    }
}
