package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.machines.PumpjackBlockEntity;
import com.st0x0ef.stellaris.common.menus.slot.FluidContainerSlot;
import com.st0x0ef.stellaris.common.menus.slot.ResultSlot;
import com.st0x0ef.stellaris.common.network.packets.SyncWidgetsTanksPacket;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class PumpjackMenu extends BaseContainer {

    private final Container container;
    private final PumpjackBlockEntity blockEntity;

    public static PumpjackMenu create(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        PumpjackBlockEntity blockEntity = (PumpjackBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos());
        return new PumpjackMenu(containerId, inventory, new SimpleContainer(4), blockEntity);
    }

    public PumpjackMenu(int containerId, Inventory inventory, Container container, PumpjackBlockEntity blockEntity) {
        super(MenuTypesRegistry.PUMPJACK_MENU.get(), containerId, 4, inventory, 18);
        this.container = container;
        this.blockEntity = blockEntity;

        // Result tank
        addSlot(new FluidContainerSlot(container, 0, 116, 30, true, true));
        addSlot(new ResultSlot(container, 1, 116, 59));
    }

    @Override
    public boolean stillValid(Player player) {
        if (!player.isLocalPlayer()) {
            syncWidgets((ServerPlayer) player);
        }
        return container.stillValid(player);
    }

    public PumpjackBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public void syncWidgets(ServerPlayer player) {
        if (!player.level().isClientSide()) {

            NetworkManager.sendToPlayer(player, new SyncWidgetsTanksPacket(
                    new long[] {blockEntity.getResultTank().getAmount(), blockEntity.chunkOilLevel()},
                    new ResourceLocation[] {blockEntity.getResultTank().getStack().getFluid().arch$registryName()}
            ));

            NetworkManager.sendToPlayer(player, new SyncWidgetsTanksPacket(
                    new long[] {blockEntity.getWrappedEnergyContainer().getStoredEnergy()}
            ));

        }
    }
}