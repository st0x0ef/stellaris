package com.st0x0ef.stellaris.common.blocks.machines;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.blocks.entities.machines.CoalGeneratorEntity;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CoalGenerator extends GeneratorBlockTemplate{

    //TODO add this because model wont work correctly otherwise
    public static final DirectionProperty FACING = AbstractFurnaceBlock.FACING;
    public static final BooleanProperty LIT = AbstractFurnaceBlock.LIT;

    public CoalGenerator(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CoalGenerator::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CoalGeneratorEntity(blockPos, blockState);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getBlock() != blockState2.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CoalGeneratorEntity) {
                Containers.dropContents(level, blockPos, (CoalGeneratorEntity)blockEntity);
                level.updateNeighbourForOutputSignal(blockPos,this);
                blockState.updateNeighbourShapes(level,blockPos,UPDATE_NEIGHBORS);
            }
            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CoalGeneratorEntity) {
                MenuRegistry.openExtendedMenu((ServerPlayer) player, this.getMenuProvider(blockState, level, blockPos));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public ExtendedMenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        return new ExtendedMenuProvider() {
            @Override
            public void saveExtraData(FriendlyByteBuf packetByteBuf) {
                packetByteBuf.writeBlockPos(blockPos);
            }

            @Override
            public Component getDisplayName() {
                return Component.translatable("block.stellaris.coal_generator");
            }

            @Override
            public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof CoalGeneratorEntity coalGeneratorEntity) {
                    return coalGeneratorEntity.createMenu(syncId, inv, player);
                }
                return null;
            }
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createGeneratorTicker(level, blockEntityType, EntityRegistry.COAL_GENERATOR.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createGeneratorTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends CoalGeneratorEntity> clientType) {
        return level.isClientSide ? null : createTickerHelper(serverType, clientType, CoalGeneratorEntity::serverTick);
    }

    @Override
    protected BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }
}
