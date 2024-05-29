package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.menus.slot.ResultSlot;
import com.st0x0ef.stellaris.common.menus.slot.SpecificItemsSlot;
import com.st0x0ef.stellaris.common.menus.slot.VacumatorCanSlot;
import com.st0x0ef.stellaris.common.menus.slot.VacumatorFoodSlot;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class VacumatorMenu extends AbstractContainerMenu {

    private final Container container;

    public VacumatorMenu(int syncId, Inventory inventory, FriendlyByteBuf buffer) {
        this(syncId, inventory, new SimpleContainer(5));
    }

    public VacumatorMenu(int syncId, Inventory inventory, Container container) {
        super(MenuTypesRegistry.VACUMATOR_MENU.get(), syncId);
        checkContainerSize(container, 5);
        this.container = container;

        addSlot(new VacumatorCanSlot(container, 0, 37, 57));
        addSlot(new VacumatorFoodSlot(container, 1, 81, 48));
        addSlot(new SpecificItemsSlot.Item(container, 2, 125, 57, Items.GLASS_BOTTLE));

        addSlot(new ResultSlot(container, 3, 58, 99));
        addSlot(new ResultSlot(container, 4, 104, 99));

        // Player hotbar
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(inventory, i, 8 + i * 18, 204));
        }

        // Player inventory
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                addSlot(new Slot(inventory, l + i * 9 + 9, 8 + l * 18, (84 + i * 18) + 62));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(originalStack, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}
