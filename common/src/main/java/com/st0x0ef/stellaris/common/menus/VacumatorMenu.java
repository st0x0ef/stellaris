package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.menus.slots.ResultSlot;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VacumatorMenu extends AbstractContainerMenu {

    private final Container inventory;

    public static VacumatorMenu create(int syncId, Inventory inventory) {

        return new VacumatorMenu(syncId, inventory, new SimpleContainer(5));
    }

    public VacumatorMenu(int syncId, Inventory playerInventory, Container container)
    {
        super(MenuTypesRegistry.VACUMATOR_MENU.get(), syncId);

        checkContainerSize(container, 5);
        this.inventory = (container);
        addSlots(inventory);

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
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
        this.addSlot(new Slot(inventory, 0, 37, 57));
        this.addSlot(new Slot(inventory, 1, 81, 48));
        this.addSlot(new Slot(inventory, 2, 125, 57));

        this.addSlot(new ResultSlot(inventory, 3, 58, 99));
        this.addSlot(new ResultSlot(inventory, 4, 104, 99));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, (84 + i * 18) + 62));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 204));
        }
    }
}
