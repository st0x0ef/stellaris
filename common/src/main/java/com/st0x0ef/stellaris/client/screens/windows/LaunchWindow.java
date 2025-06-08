package com.st0x0ef.stellaris.client.screens.windows;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.components.LaunchPadsList;
import com.st0x0ef.stellaris.client.screens.components.TexturedButton;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadUtils;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LaunchWindow extends MoveableWindow {

    public final ArrayList<TexturedButton> spaceStationButtons = new ArrayList<>();
    public final PlanetSelectionScreen parent;
    @Nullable public CelestialBody celestialBody = PlanetSelectionScreen.focusedBody;

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
        this.padsList = new LaunchPadsList(getWindowX() + 40, getWindowY() + 60, getWidth() - 80, getHeight() - 90, Component.translatable("gui.stellaris.launchpads"), this, spaceStationButtons);

        this.addWidget(this.padsList);

        TexturedButton button = new TexturedButton(((getWindowX() + getWidth()) / 2) - (15), (getWindowY() + getHeight()) - 30, 60, 20, Component.literal("Launch"), (b) -> {
            //parent.tpToFocusedPlanet(this.celestialBody);
            this.parent.setWindowVisible(1);
        })
                .tex(
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button.png"),
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button_hovered.png")
                );

        this.addWidget(button);

    }

    @Override
    public void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.clear(256, Minecraft.ON_OSX);

        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID,"textures/gui/util/window/window_large.png"), getWindowX(), getWindowY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, "Available Launch Pads", getWindowX() + getWidth() / 2, getWindowY() + 27, 0xFFFFFFFF);

        if(this.celestialBody != null && PlanetUtil.getPlanet(this.celestialBody.dimension) != null) {
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, PlanetUtil.getInLinePlanetInfo(PlanetUtil.getPlanet(this.celestialBody.dimension)), getWindowX() + getWidth() / 2 , this.padsList.getY() - 12, 0xFFFFFFFF);
        }


        this.padsList.launchPads = getLaunchPadsForDimension();
        this.padsList.launchPads.addFirst(this.addDirectLaunch());

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

        PlanetSelectionScreen.LAUNCH_PADS.launchPads()
                .stream()
                .filter((s) -> s.dimension().location() == celestialBody.dimension)
                .filter((s) -> LaunchPadUtils.canPlayerJoinLaunchPad(s, parent.getPlayer()))
                .forEach(launchPads::add);

        return launchPads;
    }

    public LaunchPad addDirectLaunch() {
        Player player = parent.getPlayer();
        return new LaunchPad(
                1000,
                this.parent.getPlayer().position(),
                ResourceKey.create(Registries.DIMENSION, celestialBody.dimension),
                "Launch Directly",
                false,
                player.getName().getString(),
                List.of()
        );
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

    public void setCelestialBody(@Nullable CelestialBody celestialBody) {
        this.celestialBody = celestialBody;
    }


}
