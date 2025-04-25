package com.st0x0ef.stellaris.client.screens.windows;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.components.TexturedButton;
import com.st0x0ef.stellaris.common.data.recipes.SpaceStationRecipe;
import com.st0x0ef.stellaris.common.data.recipes.SpaceStationRecipesManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class SpaceStationWindow extends MoveableWindow {

    public final ArrayList<TexturedButton> spaceStationButtons = new ArrayList<>();
    public final PlanetSelectionScreen parent;


    public SpaceStationWindow(int width, int height, Component message, PlanetSelectionScreen parent) {
        super(width, height, message, parent);

        this.parent = parent;
    }

    @Override
    public void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(getWindowX(), getWindowY(), getWindowX() + this.getWidth(), getWindowY() + this.getHeight(), 0xFF000000);
        parent.dragging = false;
    }

    public ArrayList<SpaceStationRecipesManager.SpaceStationRecipeState> getSpaceStationRecipeStates() {
        ArrayList<SpaceStationRecipesManager.SpaceStationRecipeState> states = new ArrayList<>();

        if(Minecraft.getInstance().player == null) {
            return states;
        }

        for (SpaceStationRecipe recipe : SpaceStationRecipesManager.SPACE_STATION_RECIPES) {
            states.add(recipe.fromRecipe(Minecraft.getInstance().player));
        }
        return states;
    }

    @Override
    public void init() {
        initSpaceStationButtons();
    }


    @Override
    public void close() {
        GLFW.glfwSetScrollCallback(Minecraft.getInstance().getWindow().getWindow(), parent::onMouseScroll);
    }


    private void initSpaceStationButtons() {
        spaceStationButtons.clear();
        AtomicInteger height = new AtomicInteger(1);
        ArrayList<SpaceStationRecipesManager.SpaceStationRecipeState> spaceStationRecipeStates = getSpaceStationRecipeStates();
        for (SpaceStationRecipesManager.SpaceStationRecipeState spaceStationRecipeState : spaceStationRecipeStates) {
            SpaceStationRecipe recipe = spaceStationRecipeState.recipe;
            int buttonWidth = 90;
            int buttonHeight = 20;

            int centerX = (this.width - 215) / 2;
            int centerY = (this.height - 177) / 2;

            int buttonX = centerX + buttonWidth / 2 - buttonWidth / 3 - buttonWidth / 15;
            int buttonY = centerY + buttonHeight / 2 + 12;

            if (spaceStationButtons.size() == 5) {
                buttonX += buttonWidth + 10;
                height.set(1);
            }

            TexturedButton button = new TexturedButton(
                    buttonX, buttonY, buttonWidth, buttonHeight,
                    Component.literal("CAXAX"),
                    (btn) -> parent.onSpaceStationButtonClick(spaceStationRecipeState)
            );

            if (spaceStationRecipeState.isUnlocked) {
                button.tex(
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button.png"),
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button_hovered.png")
                );
            } else {
                button.tex(
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/button.png"),
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/button.png")
                );
            }

            button.setPosition(buttonX, buttonY + height.getAndAdd(1) * 25);

            button.setTooltip(Tooltip.create(recipe.getTooltip(Minecraft.getInstance().player)));
            button.visible = false;
            spaceStationButtons.add(button);
            addWidget(button);

        }
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
}
