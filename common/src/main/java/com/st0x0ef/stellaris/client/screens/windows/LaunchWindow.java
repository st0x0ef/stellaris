package com.st0x0ef.stellaris.client.screens.windows;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.components.LaunchPadsList;
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
import java.util.function.Consumer;

public class LaunchWindow extends MoveableWindow {

    public final ArrayList<TexturedButton> spaceStationButtons = new ArrayList<>();
    public final PlanetSelectionScreen parent;
    @Nullable public CelestialBody celestialBody = PlanetSelectionScreen.focusedBody;
    public Map<Vector4i, LaunchPad> launchPadMap = new HashMap<>();

    private LaunchPadsList padsList;

    public LaunchWindow(int width, int height, Component message, PlanetSelectionScreen parent) {
        super(width, height, message, parent);

        this.parent = parent;
        this.moveLimit = 15;
        parent.canZoom = false;
    }


    @Override
    public void init() {
        GLFW.glfwSetScrollCallback(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().mouseHandler::onScroll);

        ArrayList<LaunchPad> spaceStationButtons = getLaunchPadsForDimension();
        this.padsList = new LaunchPadsList(getWindowX() + 40, getWindowY() + 50, getWidth() - 80, getHeight() - 80, Component.translatable("gui.stellaris.launchpads"), this, spaceStationButtons);

        this.addWidget(this.padsList);
    }

    @Override
    public void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.clear(256, Minecraft.ON_OSX);

        guiGraphics.blit(ResourceLocation.parse("stellaris:textures/gui/util/window/window_large.png"), getWindowX(), getWindowY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, "Available Launch Pads", getWindowX() + getWidth() / 2, getWindowY() + 27, 0xFFFFFFFF);

        this.padsList.launchPads = getLaunchPadsForDimension();
        this.padsList.render(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.flush();
        parent.dragging = false;
    }


    @Override
    public void close() {
        GLFW.glfwSetScrollCallback(Minecraft.getInstance().getWindow().getWindow(), parent::onMouseScroll);
        parent.canZoom = true;
        super.close();

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(padsList != null) {
            padsList.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public Consumer<MoveableWindow> resize(Minecraft minecraft, int width, int height) {
        var body = this.celestialBody;

        return (window) -> {
            if(window instanceof LaunchWindow launchWindow) {
                launchWindow.setCelestialBody(body);
                launchWindow.padsList.launchPads = launchWindow.getLaunchPadsForDimension();
            }
        };
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
