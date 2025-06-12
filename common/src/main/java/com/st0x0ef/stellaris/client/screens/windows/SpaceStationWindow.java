package com.st0x0ef.stellaris.client.screens.windows;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.GUISprites;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.components.CustomCheckBox;
import com.st0x0ef.stellaris.client.screens.components.SpaceStationList;
import com.st0x0ef.stellaris.client.screens.components.TexturedButton;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import com.st0x0ef.stellaris.common.data.recipes.SpaceStationRecipesManager;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Consumer;

public class SpaceStationWindow extends MoveableWindow {

    public final PlanetSelectionScreen parent;
    @Nullable
    public CelestialBody celestialBody = PlanetSelectionScreen.focusedBody;

    public SpaceStationRecipesManager.SpaceStationRecipeState spaceStationSelected;

    private SpaceStationList stationList;
    private EditBox nameBox;
    private CustomCheckBox publicCheckBox;
    private TexturedButton launchButton;

    public SpaceStationWindow(int width, int height, Component message, PlanetSelectionScreen parent) {
        super(width, height, message, parent);

        this.parent = parent;
    }

    @Override
    public void init() {
        this.stationList = new SpaceStationList(getWindowX() + 60, getWindowY() + 75, getWidth() - 120, getHeight() - 130, Component.translatable("gui.stellaris.launchpads"), this);
        this.addWidget(this.stationList);

        this.nameBox = new EditBox(Minecraft.getInstance().font, 150, 20, Component.literal("name"));
        this.nameBox.setPosition(this.stationList.getX() , getWindowY() + 50);
        this.nameBox.setSprites(new WidgetSprites(GUISprites.EDIT_BAR, GUISprites.EDIT_BAR));
        this.addWidget(nameBox);

        this.publicCheckBox = new CustomCheckBox(this.nameBox.getX() + this.stationList.getWidth() - 21 , getWindowY() + 50, 20,  Component.translatable("gui.stellaris.public"), Minecraft.getInstance().font, false)
                .setTexture(GUISprites.CHECKBOX, GUISprites.CHECKBOX_SELECTED)
                .showText(false);

        this.addWidget(this.publicCheckBox);

        this.launchButton = new TexturedButton((getWindowX() + getWidth()) / 2 - 1 , this.stationList.getY() + this.stationList.getHeight(), 50, 20, (button) -> this.onStationCreated()
        ).tex(
                ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button.png"),
                ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button_hovered.png")
        );
        this.addWidget(this.launchButton);

    }


    @Override
    public void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.clear(256, Minecraft.ON_OSX);
        guiGraphics.blit(ResourceLocation.parse("stellaris:textures/gui/util/window/window_large.png"), getWindowX(), getWindowY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

        guiGraphics.drawCenteredString(Minecraft.getInstance().font, "Create a Space Station", getWindowX() + getWidth() / 2, getWindowY() + 27, 0xFFFFFFFF);

        parent.dragging = false;
        guiGraphics.flush();
    }

    @Override
    public void close() {
        GLFW.glfwSetScrollCallback(Minecraft.getInstance().getWindow().getWindow(), parent::onMouseScroll);
    }

    @Override
    public Consumer<MoveableWindow> resize(Minecraft minecraft, int width, int height) {
        return (window) -> {
            if(window instanceof SpaceStationWindow) {
                stationList.spaceStationRecipeStates = SpaceStationList.getSpaceStationRecipeStates();
            }
        };
    }

    public void onStationCreated() {
        Stellaris.LOG.error("name box empty: " + this.nameBox.getValue().isEmpty());
        Stellaris.LOG.error("space station selected: " + (this.spaceStationSelected == null ? "null" : this.spaceStationSelected.recipe.location()));
        Stellaris.LOG.error("celestial body: " + (this.celestialBody == null ? "null" : this.celestialBody.dimension));

        if(!this.nameBox.getValue().isEmpty() && this.spaceStationSelected != null && this.celestialBody != null) {
            LaunchPad pad = new LaunchPad(
                    LaunchPadUtils.getNextLaunchPadId(),
                    // Will be set after
                    new Vec3(0, 0, 0),
                    ResourceKey.create(Registries.DIMENSION, this.celestialBody.dimension),
                    this.nameBox.getValue(),
                    this.publicCheckBox.selected,
                    this.parent.getPlayer().getName().toString(),
                    List.of()
            );
            this.parent.onSpaceStationButtonClick(this.celestialBody, this.spaceStationSelected, pad);
        }
    }

    public void setCelestialBody(@Nullable CelestialBody celestialBody) {
        this.celestialBody = celestialBody;
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        } else if (this.nameBox.isFocused() || this.nameBox.isHovered()) {
            return true;
            
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}
