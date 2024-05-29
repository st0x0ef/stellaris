package com.st0x0ef.stellaris.common.blocks.machines.oxygen;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenDistributorBlockEntity;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class OxygenDistributorBlock extends OxygenBlock {

    public static DirectionProperty FACING = BlockStateProperties.FACING;
    public static BooleanProperty LIT = BlockStateProperties.LIT;

    public OxygenDistributorBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof OxygenDistributorBlockEntity) {
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
                return Component.translatable("block.stellaris.oxygen_distributor");
            }

            @Override
            public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof OxygenDistributorBlockEntity oxygenDistributorBlockEntity) {
                    return oxygenDistributorBlockEntity.createMenu(syncId, inv, player);
                }
                return null;
            }
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(OxygenDistributorBlock::new);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OxygenDistributorBlockEntity(pos, state);
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
