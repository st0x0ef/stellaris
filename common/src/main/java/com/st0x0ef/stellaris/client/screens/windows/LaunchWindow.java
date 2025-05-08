package com.st0x0ef.stellaris.client.screens.windows;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.components.LaunchPadWidget;
import com.st0x0ef.stellaris.client.screens.components.TexturedButton;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LaunchWindow extends MoveableWindow {

    public final ArrayList<TexturedButton> spaceStationButtons = new ArrayList<>();
    public final PlanetSelectionScreen parent;
    @Nullable public CelestialBody celestialBody = PlanetSelectionScreen.focusedBody;
    public Map<Vector4i, LaunchPad> launchPadMap = new HashMap<>();

    public LaunchWindow(int width, int height, Component message, PlanetSelectionScreen parent) {
        super(width, height, message, parent);

        this.parent = parent;
    }

    @Override
    public void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.clear(256, Minecraft.ON_OSX);

        guiGraphics.blit(ResourceLocation.parse("stellaris:textures/gui/util/window/window_large.png"), getWindowX(), getWindowY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, "Available Launch Pads", getWindowX() + getWidth() / 2, getWindowY() + 27, 0xFFFFFFFF);

        ArrayList<LaunchPad> spaceStationButtons = getLaunchPadsForDimension();

        for(int i = 0; i < spaceStationButtons.size(); i++) {
            int x = getWindowX() + 40;
            int y = getWindowY() + 50 + (i * 35);

            LaunchPadWidget launchPadWidget = new LaunchPadWidget(spaceStationButtons.get(i), x, y, this);
            launchPadWidget.render(guiGraphics, mouseX, mouseY, partialTicks);
            launchPadMap.putIfAbsent(launchPadWidget.buttonPositions, spaceStationButtons.get(i));
        }

        guiGraphics.flush();
        parent.dragging = false;
    }


    @Override
    public void close() {
        super.close();
        GLFW.glfwSetScrollCallback(Minecraft.getInstance().getWindow().getWindow(), parent::onMouseScroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if(launchPadMap != null) {
            for(Map.Entry<Vector4i, LaunchPad> entry : launchPadMap.entrySet()) {
                Vector4i pos = entry.getKey();
                LaunchPad launchPad = entry.getValue();

                if (Utils.isHoveredOnSprite(pos.x, pos.y, pos.z, pos.w, (int) mouseX, (int) mouseY)) {
                    Stellaris.LOG.error("Clicked on launch pad {}", launchPad.position());
                    parent.tpToFocusedPlanet(launchPad.position(), celestialBody);
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public ArrayList<LaunchPad> getLaunchPadsForDimension() {
        ArrayList<LaunchPad> launchPads = new ArrayList<>();

        if(celestialBody == null) {
            return launchPads;
        }
        PlanetSelectionScreen.LAUNCH_PADS.launchPads().stream().filter((s) -> s.dimension().location() == celestialBody.dimension).forEach(launchPads::add);
        return launchPads;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return false;
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
