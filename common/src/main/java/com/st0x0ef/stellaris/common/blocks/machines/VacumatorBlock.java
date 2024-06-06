package com.st0x0ef.stellaris.common.blocks.machines;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.blocks.entities.machines.VacumatorBlockEntity;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class VacumatorBlock extends BaseMachineBlock {

    public VacumatorBlock(Properties properties) {
        super(properties);
    }

    //TODO Need new ticker
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegistry.VACUMATOR_ENTITY.get(), (level1, blockPos, state1, blockEntity) -> blockEntity.tick());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(VacumatorBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new VacumatorBlockEntity(blockPos, blockState);
    }
}
