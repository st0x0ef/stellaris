package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.machines.OxygenGeneratorBlockEntity;
import com.st0x0ef.stellaris.common.menus.slot.OxygenTankSlot;
import com.st0x0ef.stellaris.common.network.packets.SyncWidgetsTanksPacket;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class OxygenGeneratorMenu extends BaseContainer {

    private final Container container;
    private final OxygenGeneratorBlockEntity blockEntity;

    public OxygenGeneratorMenu(int containerId, Inventory inventory, Container container, OxygenGeneratorBlockEntity blockEntity) {
        super(MenuTypesRegistry.OXYGEN_DISTRIBUTOR.get(), containerId, 1, inventory, 54);
        this.container = container;
        this.blockEntity = blockEntity;

        addSlot(new OxygenTankSlot(container, 0, 17, 58));
    }

    public static OxygenGeneratorMenu create(int syncId, Inventory inventory, FriendlyByteBuf data) {
        return new OxygenGeneratorMenu(syncId, inventory, new SimpleContainer(1), (OxygenGeneratorBlockEntity) inventory.player.level().getBlockEntity(data.readBlockPos()));
    }

    public OxygenGeneratorBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        if (!player.isLocalPlayer()) {
            syncWidgets((ServerPlayer) player);
        }
        return container.stillValid(player);
    }

    private void syncWidgets(ServerPlayer player) {
        if (!player.level().isClientSide) {
            NetworkManager.sendToPlayer(player, new SyncWidgetsTanksPacket(new long[] {blockEntity.getWrappedEnergyContainer().getStoredEnergy()}));
        }
    }
}
