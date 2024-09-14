package com.st0x0ef.stellaris.common.blocks.machines;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterPumpBlockEntity;
import com.st0x0ef.stellaris.common.menus.WaterPumpMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class WaterPumpBlock extends BaseMachineBlock {

    public static final MapCodec<WaterPumpBlock> CODEC = simpleCodec(WaterPumpBlock::new);
    private static final Component TITLE = Component.translatable("block.stellaris.water_pump");

    public WaterPumpBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            MenuRegistry.openExtendedMenu((ServerPlayer) player, getMenuProvider(state, level, pos));
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @Nullable ExtendedMenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new ExtendedMenuProvider() {

            @Override
            public void saveExtraData(FriendlyByteBuf buf) {
                buf.writeBlockPos(pos);
            }

            @Override
            public Component getDisplayName() {
                return TITLE;
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof WaterPumpBlockEntity waterPumpBlockEntity) {
                    return new WaterPumpMenu(containerId, inventory, ContainerLevelAccess.create(level, pos), waterPumpBlockEntity);
                }
                return null;
            }
        };
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.WATER_PUMP.get();
    }

    @Override
    public boolean hasTicker(Level level) {
        return true;
    }
}