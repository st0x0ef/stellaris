package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.entities.vehicles.RocketEntity;
import com.st0x0ef.stellaris.common.menus.slot.ResultSlot;
import com.st0x0ef.stellaris.common.menus.slot.VehicleFuelSlot;
import com.st0x0ef.stellaris.common.menus.slot.upgrade.MotorUpgradeSlot;
import com.st0x0ef.stellaris.common.menus.slot.upgrade.RocketModelSlot;
import com.st0x0ef.stellaris.common.menus.slot.upgrade.RocketSkinSlot;
import com.st0x0ef.stellaris.common.menus.slot.upgrade.TankUpgradeSlot;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RocketMenu extends AbstractContainerMenu implements IVehicleMenu {

    private final Container inventory;
    private final RocketEntity rocket;

    public RocketMenu(int syncId, Inventory inventory, FriendlyByteBuf buffer) {
        this(syncId, inventory, new SimpleContainer(15), buffer.readVarInt());
    }

    public RocketMenu(int syncId, Inventory playerInventory, Container container, int entityId) {
        super(MenuTypesRegistry.ROCKET_MENU.get(), syncId);

        this.rocket = (RocketEntity) playerInventory.player.level().getEntity(entityId);
        checkContainerSize(container, 14);
        this.inventory = container;

        addSlots(inventory);

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        if (!player.isLocalPlayer()) {
            this.getRocket().syncRocketData((ServerPlayer) player);
        }

        return this.inventory.stillValid(player);
    }


    private void addSlots(Container inventory) {
        //FUEL SLOTS
        this.addSlot(new VehicleFuelSlot(inventory, 0, 18, 50));
        this.addSlot(new ResultSlot(inventory, 1, 18, 80));

        //UPGRADE SLOTS
        this.addSlot(new MotorUpgradeSlot(inventory, 2, 76, 94, getRocket()));
        this.addSlot(new TankUpgradeSlot(inventory, 3, 100, 94));

        //SKIN SLOTS
        this.addSlot(new RocketSkinSlot(inventory, 4, 124, 94));
        //MODEL SLOTS
        this.addSlot(new RocketModelSlot(inventory, 5, 148, 94));

        //INVENTORY SLOTS
        this.addSlot(new Slot(inventory, 6, 84, 46));
        this.addSlot(new Slot(inventory, 7, 84, 64));

        this.addSlot(new Slot(inventory, 8, 102, 46));
        this.addSlot(new Slot(inventory, 9, 102, 64));

        this.addSlot(new Slot(inventory, 10, 120, 46));
        this.addSlot(new Slot(inventory, 11, 120, 64));

        this.addSlot(new Slot(inventory, 12, 138, 46));
        this.addSlot(new Slot(inventory, 13, 138, 64));

    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 10 + l * 18, (131 + i * 18) + 11));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 10 + i * 18, 200));
        }
    }

    public RocketEntity getRocket() {
        return rocket;
    }

    @Override
    public int getFuel() {
        return getRocket().getFuel();
    }
}