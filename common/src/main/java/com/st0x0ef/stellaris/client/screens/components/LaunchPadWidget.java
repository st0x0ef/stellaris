package com.st0x0ef.stellaris.client.screens.components;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.GUISprites;
import com.st0x0ef.stellaris.client.screens.windows.MoveableWindow;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector4i;

public class LaunchPadWidget {

    public final LaunchPad launchPad;
    public final int x;
    public final int y;
    public final MoveableWindow window;

    public Vector4i buttonPositions = new Vector4i();

    public LaunchPadWidget(LaunchPad launchPad, int x, int y, MoveableWindow window) {
        this.x = x;
        this.y = y;
        this.launchPad = launchPad;
        this.window = window;
    }


    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(GUISprites.SIDEWAYS_ENERGY_FULL, this.x, this.y, window.getWidth() - 80, 30);
        guiGraphics.drawString(getFont(), launchPad.name(), this.x + 5, this.y + 5, Utils.getColorHexCode("white"));

        LaunchButton launchButton = new LaunchButton(this.x + window.getWidth() - 135, this.y + 7, 50, 15, Component.literal("Launch"), (btn) -> {
        });

        buttonPositions = new Vector4i(launchButton.getX(), launchButton.getY(), 50, 15);

        launchButton.setButtonTexture(
                ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button.png"),
                ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button_hovered.png")
        );
        launchButton.render(guiGraphics, mouseX, mouseY, partialTick);

    }


    public Font getFont() {
        return Minecraft.getInstance().font;
    }

}
