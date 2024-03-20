package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.menus.RocketStationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RocketStationScreen extends AbstractContainerScreen<RocketStationMenu> {

    public static final ResourceLocation texture = new ResourceLocation(Stellaris.MODID, "textures/gui/rocket_station.png");


    public RocketStationScreen(RocketStationMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 177;
        this.imageHeight = 224;
        this.inventoryLabelY = this.imageHeight - 92;

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

}
