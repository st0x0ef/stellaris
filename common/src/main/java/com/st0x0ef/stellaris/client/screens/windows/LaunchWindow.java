package com.st0x0ef.stellaris.client.screens.windows;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.components.LaunchPadsList;
import com.st0x0ef.stellaris.client.screens.components.TexturedButton;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadUtils;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
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
    public TexturedButton spaceStationButton;

    private LaunchPadsList padsList;

    public LaunchWindow(int width, int height, Component message, PlanetSelectionScreen parent) {
        super(width, height, message, parent);

        this.parent = parent;
        this.moveLimit = 15;
    }


    @Override
    public void init() {
        GLFW.glfwSetScrollCallback(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().mouseHandler::onScroll);

        this.padsList = new LaunchPadsList(getWindowX() + 40, getWindowY() + 60, getWidth() - 80, getHeight() - 90, Component.translatable("gui.stellaris.launchpads"), this, new ArrayList<>());
        this.addWidget(this.padsList);

        int imageRatio = 1;
        this.spaceStationButton = new TexturedButton(
                (getWindowX() + getWidth() / 2) + 73, getWindowY() + 32 - (18*imageRatio) / 2, 28*imageRatio, 18*imageRatio,
                Component.literal(""),
                (button) -> {
                    if (celestialBody != null) {
                        parent.setWindowVisible(1);
                    }
                }
        ).tex(Stellaris.id("textures/gui/util/buttons/space_station_button.png"), Stellaris.id("textures/gui/util/buttons/space_station_button_hover.png"));
        spaceStationButton.setTooltip(Tooltip.create(Component.literal("Space Stations")));

        this.addWidget(spaceStationButton);
    }

    @Override
    public void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.clear(256, Minecraft.ON_OSX);
        this.padsList.launchPads = getLaunchPadsForDimension();

        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID,"textures/gui/util/window/window_large.png"), getWindowX(), getWindowY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

        if(this.celestialBody != null && PlanetUtil.getPlanet(this.celestialBody.dimension) != null) {

            this.spaceStationButton.visible = this.celestialBody.spaceStation;

            Planet planet = PlanetUtil.getPlanet(this.celestialBody.dimension);
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, celestialBody.name + " Launch Points", getWindowX() + getWidth() / 2, getWindowY() + 27, 0xFFFFFFFF);

            guiGraphics.drawCenteredString(Minecraft.getInstance().font, PlanetUtil.getInLinePlanetInfo(planet), getWindowX() + getWidth() / 2 , this.padsList.getY() - 12, 0xFFFFFFFF);

            if(!this.parent.canLaunch(planet)) {
                guiGraphics.drawCenteredString(Minecraft.getInstance().font, "You cannot launch to this planet!", getWindowX() + getWidth() / 2, (getWindowY() + getHeight()) - 26, 0xFFFF0000);
            }


            if (this.celestialBody.canLaunchOn) {
                this.padsList.launchPads.addFirst(this.addDirectLaunch());
            } else if (this.padsList.launchPads.isEmpty()) {
                guiGraphics.drawCenteredString(Minecraft.getInstance().font, getErrorName(), getWindowX() + getWidth() / 2, this.padsList.getY() + 7, Utils.getColorHexCode("white"));
            }
        }

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
        Planet planet = PlanetUtil.getPlanet(celestialBody.dimension);

        if(planet == null) {
            return launchPads;
        }
        PlanetSelectionScreen.LAUNCH_PADS.launchPads()
                .stream()
                .filter((pad) -> pad.dimension().location() == planet.dimension() || (planet.orbit().isPresent() && pad.dimension().location().equals(planet.orbit().get())))
                .filter((pad) -> LaunchPadUtils.canPlayerJoinLaunchPad(pad, parent.getPlayer()))
                .forEach(launchPads::add);

        return launchPads;
    }

    public LaunchPad addDirectLaunch() {
        Player player = parent.getPlayer();
        return new LaunchPad(
                10000,
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

    public String getErrorName() {
        double randomNumber = (Math.random() * 100) + 1;
        if (randomNumber < 55) {
            return "No Launch Pads available";
        } else if (randomNumber < 99) {
            return "Error 404: Launchpads not found";
        } else {
            return "These are not the Launchpads you are looking for";
        }
    }

    @Override
    public int getMoveLimit() {
        return 15;
    }
}
