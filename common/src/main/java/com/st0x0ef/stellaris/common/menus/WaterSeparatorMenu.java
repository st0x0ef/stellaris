package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class WaterSeparatorMenu extends AbstractContainerMenu {

    public WaterSeparatorMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory, new SimpleContainer(6));
    }

    public WaterSeparatorMenu(int containerId, Inventory inventory, Container container) {
        super(MenuTypesRegistry.WATER_SEPARATOR_MENU.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
