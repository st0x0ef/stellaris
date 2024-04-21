package com.st0x0ef.stellaris.common.blocks.machines;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.blocks.entities.machines.CoalGeneratorEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.SolarPanelEntity;
import com.st0x0ef.stellaris.common.menus.CoalGeneratorMenu;
import com.st0x0ef.stellaris.common.menus.SolarPanelMenu;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoalGenerator extends GeneratorBlockTemplate{
    public CoalGenerator(Properties properties) {
        super(properties);
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
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
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
                return Component.literal("Coal Generator");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                return CoalGeneratorMenu.create(syncId, inv, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(blockPos));
            }
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
