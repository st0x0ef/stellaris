package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class GalaxyMenu extends AbstractContainerMenu {
    private final Player player;
    public boolean freeze_gui = false;

    public static GalaxyMenu create(int syncId, Inventory inventory, FriendlyByteBuf data) {

        return new GalaxyMenu(syncId, inventory, new SimpleContainer(0));
    }
    public GalaxyMenu(int syncId, Inventory playerInventory, Container container)
    {
        super(MenuTypesRegistry.GALAXY_MENU.get(), syncId);
        this.player = playerInventory.player;
    }
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        return ItemStack.EMPTY;
    }
    @Override
    public boolean stillValid(Player player) {
        return !player.isDeadOrDying();
    }

    public Player getPlayer() {
        return player;
    }

    public void switchFreezeGui() {
        freeze_gui = !freeze_gui;
    }
}

