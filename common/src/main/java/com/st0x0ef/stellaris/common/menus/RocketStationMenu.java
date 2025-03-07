package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.menus.slot.ResultSlot;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RocketStationMenu extends AbstractContainerMenu {

    private final Container inventory;
    public RocketStationMenu(int syncId, Inventory inventory, FriendlyByteBuf buffer) {
        this(syncId, inventory, new SimpleContainer(15));
    }

    public RocketStationMenu(int syncId, Inventory playerInventory, Container container)
    {
        super(MenuTypesRegistry.ROCKET_STATION.get(), syncId);

        checkContainerSize(container, 15);
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
        return this.inventory.stillValid(player);
    }


    private void addSlots(Container inventory) {
        this.addSlot(new Slot(inventory, 0, 51, 12));

        this.addSlot(new Slot(inventory, 1, 42, 30));
        this.addSlot(new Slot(inventory, 2, 60, 30));

        this.addSlot(new Slot(inventory, 3, 42, 48));
        this.addSlot(new Slot(inventory, 4, 60, 48));

        this.addSlot(new Slot(inventory, 5, 42, 66));
        this.addSlot(new Slot(inventory, 6, 60, 66));

        this.addSlot(new Slot(inventory, 7, 24, 84));
        this.addSlot(new Slot(inventory, 8, 42, 84));
        this.addSlot(new Slot(inventory, 9, 60, 84));
        this.addSlot(new Slot(inventory, 10, 78, 84));

        this.addSlot(new Slot(inventory, 11, 24, 102));
        this.addSlot(new Slot(inventory, 12, 51, 102));
        this.addSlot(new Slot(inventory, 13, 78, 102));

        this.addSlot(new ResultSlot(inventory, 14, 128, 52));

    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 10 + l * 18, (84 + i * 18) + 58));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 10 + i * 18, 200));
        }
    }
}
