package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.entities.vehicles.RoverEntity;
import com.st0x0ef.stellaris.common.menus.slot.ResultSlot;
import com.st0x0ef.stellaris.common.menus.slot.VehicleFuelSlot;
import com.st0x0ef.stellaris.common.menus.slot.upgrade.MotorUpgradeSlot;
import com.st0x0ef.stellaris.common.menus.slot.upgrade.SpeedUpgradeSlot;
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

public class RoverMenu extends AbstractContainerMenu implements IVehicleMenu
{

    private final Container inventory;
    private final RoverEntity rover;

    public RoverMenu(int syncId, Inventory inventory, FriendlyByteBuf buffer) {
        this(syncId, inventory, new SimpleContainer(13), buffer.readVarInt());
    }

    public RoverMenu(int syncId, Inventory playerInventory, Container container, int entityId) {
        super(MenuTypesRegistry.ROVER_MENU.get(), syncId);

        this.rover = (RoverEntity) playerInventory.player.level().getEntity(entityId);
        checkContainerSize(container, 13);
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
            this.getRover().syncRocketData((ServerPlayer) player);
        }

        return this.inventory.stillValid(player);
    }


    private void addSlots(Container inventory) {
        //FUEL SLOTS
        this.addSlot(new VehicleFuelSlot(inventory, 0, 20, 28));
        this.addSlot(new ResultSlot(inventory, 1, 20, 62));

        //UPGRADE SLOTS
        this.addSlot(new MotorUpgradeSlot(inventory, 2, 82, 74, this.rover));
        this.addSlot(new SpeedUpgradeSlot(inventory,3,109,74));
        this.addSlot(new TankUpgradeSlot(inventory, 4, 136, 74));

        //INVENTORY SLOTS
        this.addSlot(new Slot(inventory, 5, 82, 28));
        this.addSlot(new Slot(inventory, 6, 82, 46));

        this.addSlot(new Slot(inventory, 7, 100, 28));
        this.addSlot(new Slot(inventory, 8, 100, 46));

        this.addSlot(new Slot(inventory, 9, 118, 28));
        this.addSlot(new Slot(inventory, 10, 118, 46));

        this.addSlot(new Slot(inventory, 11, 136, 28));
        this.addSlot(new Slot(inventory, 12, 136, 46));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 10 + l * 18, (95 + i * 18) + 11));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 10 + i * 18, 164));
        }
    }

    public RoverEntity getRover() {
        return rover;
    }

    @Override
    public int getFuel() {
        return getRover().getFuel();
    }
}
