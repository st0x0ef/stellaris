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

public class LanderMenu extends AbstractContainerMenu {
    private final Container inventory;

    public LanderMenu(int syncId, Inventory inventory, FriendlyByteBuf buffer) {
        this(syncId, inventory, new SimpleContainer(15));
    }

    public LanderMenu(int syncId, Inventory playerInventory, Container container)
    {
        super(MenuTypesRegistry.LANDER_MENU.get(), syncId);

        checkContainerSize(container, 15);
        this.inventory = (container);

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
        //FUEL SLOTS
        this.addSlot(new ResultSlot(inventory, 0, 24, 68));
        this.addSlot(new ResultSlot(inventory, 1, 52, 68));

        //UPGRADE SLOTS
        this.addSlot(new ResultSlot(inventory, 2, 82, 74));
        this.addSlot(new ResultSlot(inventory, 3, 100, 74));
        this.addSlot(new ResultSlot(inventory, 4, 118, 74));
        this.addSlot(new ResultSlot(inventory, 5, 136, 74));

        //INVENTORY SLOTS
        this.addSlot(new ResultSlot(inventory, 6, 82, 28));
        this.addSlot(new ResultSlot(inventory, 7, 82, 46));

        this.addSlot(new ResultSlot(inventory, 8, 100, 28));
        this.addSlot(new ResultSlot(inventory, 9, 100, 46));

        this.addSlot(new ResultSlot(inventory, 10, 118, 28));
        this.addSlot(new ResultSlot(inventory, 11, 118, 46));

        this.addSlot(new ResultSlot(inventory, 12, 136, 28));
        this.addSlot(new ResultSlot(inventory, 13, 136, 46));

        //ROCKET SLOT
        this.addSlot(new ResultSlot(inventory, 14, 38, 37));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 10 + l * 18, (98 + i * 18) + 8));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 10 + i * 18, 164));
        }
    }
}