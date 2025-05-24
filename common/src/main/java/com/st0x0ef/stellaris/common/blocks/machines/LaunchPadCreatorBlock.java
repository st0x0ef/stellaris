package com.st0x0ef.stellaris.common.blocks.machines;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.machines.LaunchPadCreatorBlockEntity;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.menus.LaunchPadCreatorMenu;
import com.st0x0ef.stellaris.common.network.packets.LaunchPadsOperations;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LaunchPadCreatorBlock extends BaseMachineBlock {

    public LaunchPadCreatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LaunchPadCreatorBlockEntity(pos, state);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.LAUNCHPAD_CREATOR.get();
    }

    @Override
    public boolean hasTicker(Level level) {
        return !level.isClientSide;
    }


    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(LaunchPadCreatorBlock::new);
    }


    @Nullable
    @Override
    protected ExtendedMenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof LaunchPadCreatorBlockEntity padCreatorBlock) {
            return new ExtendedMenuProvider() {
                @Override
                public void saveExtraData(FriendlyByteBuf buf) {
                    buf.writeBlockPos(blockEntity.getBlockPos());
                    buf.writeInt(padCreatorBlock.launchPadId);
                }

                @Override
                public Component getDisplayName() {
                    return padCreatorBlock.getDisplayName();
                }

                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

                    buf.writeBlockPos(blockEntity.getBlockPos());
                    buf.writeInt(padCreatorBlock.launchPadId);

                    return LaunchPadCreatorMenu.create(containerId, inventory, buf);
                }
            };
        }
        return null;
    }


}
