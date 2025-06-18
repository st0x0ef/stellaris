package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class PlanetSelectionMenu extends AbstractContainerMenu {
    private final Player player;
    private final boolean forceCanGoTo;
    private final String galaxyId;
    public boolean freeze_gui = false;

    public PlanetSelectionMenu(int syncId, Inventory playerInventory, boolean forceCanGoTo, String galaxyId) {
        super(MenuTypesRegistry.PLANET_SELECTION_MENU.get(), syncId);
        this.player = playerInventory.player;
        this.forceCanGoTo = forceCanGoTo;
        this.galaxyId = galaxyId;
    }

    public static PlanetSelectionMenu create(int syncId, Inventory inventory, FriendlyByteBuf data) {
        boolean forceCanGoTo = data.readBoolean();
        String galaxyId = data.readUtf();
        return new PlanetSelectionMenu(syncId, inventory, forceCanGoTo, galaxyId);
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

    public boolean getForceCanGoTo() {
        return forceCanGoTo;
    }

    public String getGalaxyId() {
        return galaxyId;
    }
}
