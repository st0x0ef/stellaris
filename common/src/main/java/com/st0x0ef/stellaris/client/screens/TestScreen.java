package com.st0x0ef.stellaris.client.screens;

import com.st0x0ef.stellaris.client.screens.windows.TestWindow;
import com.st0x0ef.stellaris.common.menus.TabletMenu;
import com.st0x0ef.stellaris.common.menus.TestMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TestScreen extends BaseWindowScreen<TestMenu> {


    public TestScreen(TestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }


    @Override
    protected void init() {
        super.init();

        TestWindow testWindow = new TestWindow(200, 100, Component.literal("Test Window"), this);
        this.addRenderableWidget(testWindow);
    }



}
