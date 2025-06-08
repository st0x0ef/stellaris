package com.st0x0ef.stellaris.client.screens.windows;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.components.CustomCheckBox;
import com.st0x0ef.stellaris.client.screens.components.SpaceStationList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class SpaceStationWindow extends MoveableWindow {

    public final PlanetSelectionScreen parent;

    public ResourceLocation spaceStationSelected = ResourceLocation.parse("stellaris:null");

    private SpaceStationList stationList;
    private EditBox nameBox;


    public SpaceStationWindow(int width, int height, Component message, PlanetSelectionScreen parent) {
        super(width, height, message, parent);

        this.parent = parent;
    }

    @Override
    public void init() {
        this.stationList = new SpaceStationList(getWindowX() + 60, getWindowY() + 75, getWidth() - 120, getHeight() - 120, Component.translatable("gui.stellaris.launchpads"), this);
        this.addWidget(this.stationList);

        this.nameBox = new EditBox(Minecraft.getInstance().font, 100, 20, Component.literal("name"));
        //this.nameBox.setBordered(false);
        this.nameBox.setPosition(this.stationList.getX() , getWindowY() + 50);
        this.addWidget(nameBox);

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
