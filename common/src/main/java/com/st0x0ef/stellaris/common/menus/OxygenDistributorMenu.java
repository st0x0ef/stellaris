package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenDistributorBlockEntity;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class OxygenDistributorMenu extends BaseContainer {

    private final Container container;
    private final OxygenDistributorBlockEntity blockEntity;

    public OxygenDistributorMenu(int containerId, Inventory inventory, Container container, OxygenDistributorBlockEntity blockEntity) {
        super(MenuTypesRegistry.OXYGEN_DISTRIBUTOR.get(), containerId, 2, inventory);
        this.container = container;
        this.blockEntity = blockEntity;

        // TODO change slot positions to match gui texture
        addSlot(new Slot(container, 0, 50, 25));
        addSlot(new Slot(container, 1, 68, 25));
    }

    public static OxygenDistributorMenu create(int syncId, Inventory inventory, FriendlyByteBuf data) {
        OxygenDistributorBlockEntity entity = (OxygenDistributorBlockEntity) inventory.player.level().getBlockEntity(data.readBlockPos());
        return new OxygenDistributorMenu(syncId, inventory, new SimpleContainer(2), entity);
    }

    public OxygenDistributorBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}
