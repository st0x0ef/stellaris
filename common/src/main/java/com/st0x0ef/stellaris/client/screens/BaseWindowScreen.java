package com.st0x0ef.stellaris.client.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class BaseWindowScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public BaseWindowScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        for(Renderable renderable : this.renderables) {
            if(renderable instanceof AbstractWidget widget) {
                widget.mouseReleased(mouseX, mouseY, button);
            }
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for(Renderable renderable : this.renderables) {
            if(renderable instanceof AbstractWidget widget) {
                widget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
}
