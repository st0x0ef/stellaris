package com.st0x0ef.stellaris.client.screens.components;

import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;


public class LaunchPadWidget extends AbstractWidget {


    public LaunchPadWidget(LaunchPad launchPad, int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal(launchPad.name()));
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

}
