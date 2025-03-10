package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.blocks.entities.machines.PowerBankEntity;
import com.st0x0ef.stellaris.common.menus.slot.EnergySlot;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class PowerBankMenu extends BaseContainer {

    private final Container inventory;
    private final PowerBankEntity blockEntity;

    public static PowerBankMenu create(int syncId, Inventory inventory, FriendlyByteBuf data) {
        return new PowerBankMenu(syncId, inventory, new SimpleContainer(2), (PowerBankEntity) inventory.player.level().getBlockEntity(data.readBlockPos()));
    }

    public PowerBankMenu(int syncId, Inventory playerInventory, Container container, PowerBankEntity entity) {
        super(MenuTypesRegistry.POWER_BANK_MENU.get(), syncId, 2, playerInventory, 22);

        checkContainerSize(container, 2);
        this.inventory = container;
        this.blockEntity = entity;

        // TODO real x,y values
        addSlot(new EnergySlot(inventory, 0, 38, 44)); // INSERT
        addSlot(new EnergySlot(inventory, 1, 55, 44)); // EXTRACT

    }

    public PowerBankEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }
}
