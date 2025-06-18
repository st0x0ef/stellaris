package com.st0x0ef.stellaris.client.screens.windows;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TestWindow extends MoveableWindow {

    public TestWindow(int width, int height, Component message, Screen parent) {
        super(width, height, message, parent);
    }

    @Override
    public void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        guiGraphics.fill(getWindowX(), getWindowY(), getWindowX() + this.getWidth(), getWindowY() + this.getHeight(), 0xFF000000);

        guiGraphics.drawString(Minecraft.getInstance().font, "Test Window", this.getWindowX() + 10, this.getWindowY() + 10, 0xffffff);


    }

    @Override
    public void init() {
        Button button = Button.builder(Component.literal("Test"), (button1) -> System.out.println("Button clicked!")).bounds(getWindowX() + 10, getWindowY() + 30, 100, 20).build();

        this.addWidget(button);

    }

}
