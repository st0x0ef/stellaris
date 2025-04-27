package com.st0x0ef.stellaris.client.screens.windows;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.components.TexturedButton;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class LaunchWindow extends MoveableWindow {

    public final ArrayList<TexturedButton> spaceStationButtons = new ArrayList<>();
    public final PlanetSelectionScreen parent;
    @Nullable public CelestialBody celestialBody = PlanetSelectionScreen.focusedBody;

    public LaunchWindow(int width, int height, Component message, PlanetSelectionScreen parent) {
        super(width, height, message, parent);

        this.parent = parent;
    }

    @Override
    public void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(getWindowX(), getWindowY(), getWindowX() + this.getWidth(), getWindowY() + this.getHeight(), 0xFF000000);

        guiGraphics.drawString(Minecraft.getInstance().font, "Available Launch Pads", getWindowX() + 5, getWindowY() + 5, 0xFFFFFFFF);
        ArrayList<LaunchPad> spaceStationButtons = getLaunchPadsForDimension();

        for(int i = 0; i < spaceStationButtons.size(); i++) {
            LaunchPad launchPad = spaceStationButtons.get(i);
            String name = launchPad.name();
            int x = getWindowX() + 5;
            int y = getWindowY() + 20 + (i * 20);
            guiGraphics.drawString(Minecraft.getInstance().font, name ,x, y, 0xFFFFFFFF);
        }



        parent.dragging = false;
    }


    @Override
    public void close() {
        GLFW.glfwSetScrollCallback(Minecraft.getInstance().getWindow().getWindow(), parent::onMouseScroll);
    }


    public ArrayList<LaunchPad> getLaunchPadsForDimension() {
        ArrayList<LaunchPad> launchPads = new ArrayList<>();

        if(celestialBody == null) {
            return launchPads;
        }
        PlanetSelectionScreen.LAUNCH_PADS.launchPads().stream().filter((s) -> s.dimension().location() == celestialBody.dimension).forEach(launchPads::add);
        return (ArrayList<LaunchPad>) PlanetSelectionScreen.LAUNCH_PADS.launchPads();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void changeVisibility(boolean visible) {
        super.changeVisibility(visible);
        for(TexturedButton button : spaceStationButtons) {
            button.visible = visible;
        }
    }

    public void setCelestialBody(CelestialBody celestialBody) {
        this.celestialBody = celestialBody;
        Stellaris.LOG.error("Setting celestial body to {}", celestialBody.name);
    }
}
