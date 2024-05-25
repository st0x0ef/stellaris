package com.st0x0ef.stellaris.client.screens.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class InvisibleButton extends Button {

    private Runnable onHoverAction;

    public InvisibleButton(int x, int y, int width, int height, Component title, OnPress onPress, Runnable onHoverAction) {
        super(x, y, width, height, title, onPress, Button.DEFAULT_NARRATION);
        this.onHoverAction = onHoverAction;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isHovered) {
            onHover(mouseX, mouseY);
        }
    }

    public void onHover(double mouseX, double mouseY) {
        if (onHoverAction != null) {
            onHoverAction.run();
        }
    }
}
