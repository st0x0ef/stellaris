package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.RocketStationEntity;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;

public class RocketStationMenu extends AbstractContainerMenu {

    private final Container inventory;
    public RocketStationMenu(int syncId, Inventory inventory) {
        this(syncId, inventory, new SimpleContainer(15));
    }

    public RocketStationMenu(int syncId, Inventory playerInventory, Container container)
    {

        super(MenuTypesRegistry.ROCKET_STATION.get(), syncId);


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
        this.addSlot(new Slot(inventory, 0, 56, 20));

        this.addSlot(new Slot(inventory, 1, 47, 38));
        this.addSlot(new Slot(inventory, 3, 47, 56));
        this.addSlot(new Slot(inventory, 5, 47, 75));

        this.addSlot(new Slot(inventory, 2, 65, 38));
        this.addSlot(new Slot(inventory, 4, 65, 56));
        this.addSlot(new Slot(inventory, 6, 65, 75));

        this.addSlot(new Slot(inventory, 7, 29, 92));
        this.addSlot(new Slot(inventory, 8, 47, 92));
        this.addSlot(new Slot(inventory, 9, 65, 92));
        this.addSlot(new Slot(inventory, 10, 83, 92));

        this.addSlot(new Slot(inventory, 11, 29, 110));
        this.addSlot(new Slot(inventory, 12, 56, 110));
        this.addSlot(new Slot(inventory, 13, 83, 110));

        this.addSlot(new ResultSlot(inventory, 14, 129, 56));

    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, (84 + i * 18) + 58));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 200));
        }
    }
}
