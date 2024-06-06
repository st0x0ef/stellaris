package com.st0x0ef.stellaris.common.blocks.machines.oxygen;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenDistributorBlockEntity;
import com.st0x0ef.stellaris.common.blocks.machines.BaseLitMachineBlock;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class OxygenDistributorBlock extends BaseLitMachineBlock {

    public OxygenDistributorBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BlockEntityRegistry.OXYGEN_DISTRIBUTOR.get(), (level1, pos, state1, blockEntity) -> blockEntity.tick());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(OxygenDistributorBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OxygenDistributorBlockEntity(pos, state);
    }
}
