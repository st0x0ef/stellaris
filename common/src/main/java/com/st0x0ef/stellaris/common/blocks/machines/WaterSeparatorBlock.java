package com.st0x0ef.stellaris.common.blocks.machines;

import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class WaterSeparatorBlock extends BaseEnergyBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public WaterSeparatorBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof WaterSeparatorBlockEntity waterSeparatorBlockEntity) {
                Containers.dropContents(level, pos, waterSeparatorBlockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
                state.updateNeighbourShapes(level, pos, UPDATE_NEIGHBORS);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof WaterSeparatorBlockEntity) {
                MenuRegistry.openExtendedMenu((ServerPlayer) player, getMenuProvider(state, level, pos));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    protected ExtendedMenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new ExtendedMenuProvider() {

            @Override
            public void saveExtraData(FriendlyByteBuf buf) {
            }

            @Override
            public Component getDisplayName() {
                return Component.translatable("block.stellaris.water_separator");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof WaterSeparatorBlockEntity waterSeparatorBlockEntity) {
                    return waterSeparatorBlockEntity.createMenu(syncId, inventory, player);
                }
                return null;
            }
        };
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
