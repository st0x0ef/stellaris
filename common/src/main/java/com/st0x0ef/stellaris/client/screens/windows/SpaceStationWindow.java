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

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class SpaceStationWindow extends MoveableWindow{

    public final ArrayList<TexturedButton> spaceStationButtons = new ArrayList<>();
    public final ArrayList<SpaceStationRecipesManager.SpaceStationRecipeState> spaceStationRecipeStates = new ArrayList<>();
    public final PlanetSelectionScreen parent;
    public final Player player;

    public SpaceStationWindow(int width, int height, Component message, PlanetSelectionScreen parent) {
        super(width, height, message, parent);

        this.player = parent.getPlayer();
        this.parent = parent;
    }

    @Override
    public void renderWindow(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    public void init() {
        for (SpaceStationRecipe recipe : SpaceStationRecipesManager.SPACE_STATION_RECIPES) {
            this.spaceStationRecipeStates.add(recipe.fromRecipe(this.player));
        }

        initSpaceStationButtons();

    }

    private void initSpaceStationButtons() {
        spaceStationButtons.clear();
        AtomicInteger height = new AtomicInteger(1);
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
                    Component.translatable(String.valueOf(recipe.location())),
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

            button.setTooltip(Tooltip.create(recipe.getTooltip(this.player)));
            button.visible = true;
            spaceStationButtons.add(button);
            addWidget(button);

        }
    }
}
