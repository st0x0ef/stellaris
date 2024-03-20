package com.st0x0ef.stellaris.common.blocks;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.RocketStationEntity;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class RocketStation extends BaseEntityBlock {

    public static final MapCodec<RocketStation> CODEC = simpleCodec(RocketStation::new);

    public RocketStation(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends RocketStation> codec() {
        return CODEC;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof RocketStationEntity) {
                player.openMenu(blockState.getMenuProvider(level, blockPos));

            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RocketStationEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, EntityRegistry.ROCKET_STATION.get(),
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getBlock() != blockState2.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof RocketStationEntity) {
                Containers.dropContents(level, blockPos, (RocketStationEntity)blockEntity);
                level.updateNeighbourForOutputSignal(blockPos,this);
            }
            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }
    }
}
