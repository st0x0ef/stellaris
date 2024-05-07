package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.ModifiedButton;
import com.st0x0ef.stellaris.client.screens.helper.ScreenHelper;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Environment(EnvType.CLIENT)
public class PlanetSelectionScreen extends AbstractContainerScreen<PlanetSelectionMenu> {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/planet_selection.png");
    public static final ResourceLocation SCROLLER_TEXTURE = new ResourceLocation(Stellaris.MODID,
            "textures/gui/util/scroller.png");

    public static final ResourceLocation SMALL_BUTTON_TEXTURE = new ResourceLocation(Stellaris.MODID,
            "textures/gui/util/buttons/small_button.png");
    public static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(Stellaris.MODID,
            "textures/gui/util/buttons/button.png");
    public static final ResourceLocation LARGE_BUTTON_TEXTURE = new ResourceLocation(Stellaris.MODID,
            "textures/gui/util/buttons/large_button.png");

    public static final ResourceLocation MILKY_WAY_TEXTURE = new ResourceLocation(Stellaris.MODID,
            "textures/gui/util/milky_way.png");

    public static final ResourceLocation SMALL_MENU_LIST = new ResourceLocation(Stellaris.MODID,
            "textures/gui/util/planet_menu.png");
    public static final ResourceLocation LARGE_MENU_TEXTURE = new ResourceLocation(Stellaris.MODID,
            "textures/gui/util/large_planet_menu.png");

    public List<ModifiedButton> visibleButtons;

    public PlanetSelectionScreen(PlanetSelectionMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 1200;
        this.imageHeight = 1600;
        this.inventoryLabelY = this.imageHeight - 110;

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }


    @Override
    protected void renderBg(GuiGraphics graphics, float var2, int var3, int var4) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        graphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);


        ScreenHelper.drawTexture(0, (this.height / 2) - 177 / 2, 215, 177, LARGE_MENU_TEXTURE, true);
        AtomicInteger systemsHeight = new AtomicInteger();
        StellarisData.SYSTEMS.forEach((key, value) -> {
            Button systemsButton = Button.builder(Component.literal(key), (button) -> {
                System.out.println("System: " + key);
            }).width(200).pos(2, 90 + systemsHeight.get()).build();
            System.out.println("System: " + key);
            systemsHeight.addAndGet(20);
            addRenderableWidget(systemsButton);
        });

    }

    @Override
    public PlanetSelectionMenu getMenu() {
        return this.menu;
    }


    public void addSystemsButtons() {

    }

    public ModifiedButton addButton(int x, int y, int row, int width, int height, boolean rocketCondition,
                                    ModifiedButton.ButtonTypes type, List<String> list, ResourceLocation buttonTexture,
                                    ModifiedButton.ColorTypes colorType, Component title, Button.OnPress onPress) {
        return this.addRenderableWidget(new ModifiedButton(x, y, row, width, height, 0, 0, 0,
                rocketCondition, type, list, buttonTexture, colorType, width, height, onPress, title));
    }

}
