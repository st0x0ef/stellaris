package com.st0x0ef.stellaris.client.screens.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.GUISprites;
import com.st0x0ef.stellaris.client.screens.windows.SpaceStationWindow;
import com.st0x0ef.stellaris.common.data.recipes.SpaceStationRecipe;
import com.st0x0ef.stellaris.common.data.recipes.SpaceStationRecipesManager;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector4i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SpaceStationList extends AbstractScrollWidget {

    private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "icon/scroller");

    public Map<Vector4i, SpaceStationRecipesManager.SpaceStationRecipeState> spaceStationRecipeStateMap = new HashMap<>();
    private final AtomicInteger finalHeight = new AtomicInteger(0);
    private final SpaceStationWindow window;
    public ArrayList<SpaceStationRecipesManager.SpaceStationRecipeState> spaceStationRecipeStates;

    public SpaceStationList(int x, int y, int width, int height, Component message, SpaceStationWindow window) {
        super(x, y, width, height, message);
        this.window = window;
        this.spaceStationRecipeStates = getSpaceStationRecipeStates();
    }

    @Override
    protected int getInnerHeight() {
        return finalHeight.get() / 2;
    }

    @Override
    protected void renderBorder(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        //We don't want to render the border
    }

    @Override
    protected double scrollRate() {
        return 9;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        finalHeight.set(0);

        for(int i = 0; i < this.spaceStationRecipeStates.size(); i++) {
            int x = this.window.getWindowX() + 40;
            int y = (i * 35);

            SpaceStationRecipesManager.SpaceStationRecipeState recipeState = this.spaceStationRecipeStates.get(i);


            SpaceStationButton launchPadWidget = new SpaceStationButton(recipeState, x, getY() + y, this.width + 19, this.window);
            launchPadWidget.render(guiGraphics, mouseX, (int) (mouseY + this.scrollAmount()), partialTick);
            spaceStationRecipeStateMap.putIfAbsent(launchPadWidget.buttonPositions, recipeState);

            finalHeight.addAndGet(y);
        }
    }

    @Override
    public boolean isHovered() {
        return super.isHovered();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    public void renderScrollBar(GuiGraphics guiGraphics) {
        int i = this.getScrollBarHeight();
        int j = this.getX() + this.width;

        int k = Math.max(this.getY(), (int) this.scrollAmount() * (this.height - i) / this.getMaxScrollAmount() + this.getY());
        RenderSystem.enableBlend();
        guiGraphics.blitSprite(SCROLLER_SPRITE, j, k, 8, i);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if(spaceStationRecipeStateMap != null) {
            for(Map.Entry<Vector4i, SpaceStationRecipesManager.SpaceStationRecipeState> entry : spaceStationRecipeStateMap.entrySet()) {
                Vector4i pos = entry.getKey();
                SpaceStationRecipesManager.SpaceStationRecipeState state = entry.getValue();

                if (Utils.isHoveredOnSprite(pos.x, (int) (pos.y - this.scrollAmount()), pos.z, pos.w, (int) mouseX, (int) mouseY)) {
                    if(state.isUnlocked) {
                        window.spaceStationSelected = state.recipe.location();
                    }

                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    public Font getFont() {
        return Minecraft.getInstance().font;
    }

    public static ArrayList<SpaceStationRecipesManager.SpaceStationRecipeState> getSpaceStationRecipeStates() {
        ArrayList<SpaceStationRecipesManager.SpaceStationRecipeState> states = new ArrayList<>();

        if(Minecraft.getInstance().player == null) {
            return states;
        }

        for (SpaceStationRecipe recipe : SpaceStationRecipesManager.SPACE_STATION_RECIPES) {
            states.add(recipe.fromRecipe(Minecraft.getInstance().player));
        }
        return states;
    }

    public static class SpaceStationButton {

        public final SpaceStationRecipesManager.SpaceStationRecipeState recipeState;
        public final int x;
        public final int y;
        public final int width;
        public final SpaceStationWindow window;

        public Vector4i buttonPositions = new Vector4i();

        public SpaceStationButton(SpaceStationRecipesManager.SpaceStationRecipeState recipeState, int x, int y, int width, SpaceStationWindow window) {
            this.x = x;
            this.y = y;
            this.recipeState = recipeState;
            this.window = window;
            this.width = width;
        }

        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.blitSprite(GUISprites.WINDOW_BAR, this.x + 20, this.y, this.width - 20, 30);
            guiGraphics.drawString(getFont(), recipeState.recipe.getDisplayName(), this.x + 27, this.y + 10, Utils.getColorHexCode("white"));

            TexturedButton launchButton = new TexturedButton((this.x + this.width) - 54, this.y + 6, 49, 18, Component.literal("Select"), (btn) -> {
                if(recipeState.isUnlocked) {
                    window.spaceStationSelected = recipeState.recipe.location();
                }
            });

            launchButton.setTooltip(Tooltip.create(this.recipeState.recipe.getTooltip(this.window.parent.getPlayer())));

            buttonPositions = new Vector4i(launchButton.getX(), launchButton.getY(), launchButton.getWidth(), launchButton.getHeight());

            if (recipeState.isUnlocked) {
                launchButton.tex(
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/select_button.png"),
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/select_button_hovered.png")
                );
            } else {
                launchButton.tex(
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/select_button.png"),
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/select_button.png")
                );
            }


            launchButton.render(guiGraphics, mouseX, mouseY, partialTick);

            if (Utils.isHoveredOnSprite(this.x, this.y, window.getWidth() - 80, 30, mouseX, mouseY)) {
                guiGraphics.renderTooltip(getFont(), launchButton.getTooltip().toCharSequence(Minecraft.getInstance() ), mouseX, mouseY);

            }
        }

        public Font getFont() {
            return Minecraft.getInstance().font;
        }

    }
}
