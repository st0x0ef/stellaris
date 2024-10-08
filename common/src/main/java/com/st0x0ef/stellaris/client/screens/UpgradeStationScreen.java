package com.st0x0ef.stellaris.client.screens;

import com.st0x0ef.stellaris.common.menus.UpgradeStationMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class UpgradeStationScreen extends ItemCombinerScreen<UpgradeStationMenu> {

    private static final ResourceLocation ANVIL_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/container/anvil.png"); //temporary

    public UpgradeStationScreen(UpgradeStationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ANVIL_LOCATION);
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int x, int y) {

    }
}
