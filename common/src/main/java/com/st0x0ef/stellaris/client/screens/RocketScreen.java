package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.entities.RocketEntity;
import com.st0x0ef.stellaris.common.menus.RocketMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RocketScreen extends AbstractContainerScreen<RocketMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/rocket.png");

    private final RocketEntity rocket = getMenu().getRocket();
    private Gauge fuelGauge;

    public RocketScreen(RocketMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        imageWidth = 177;
        imageHeight = 177;
        inventoryLabelY = imageHeight - 92;
    }

    @Override
    protected void init() {
        super.init();

        if (rocket == null) {
            return;
        }

        fuelGauge = new Gauge(leftPos + 51, topPos + 27, 12, 46, Component.translatable("stellaris.screen.fuel"), GUISprites.FUEL_OVERLAY, GUISprites.FLUID_TANK_OVERLAY, rocket.rocketComponent.getFuel(), rocket.rocketComponent.getTankCapacity());
        addRenderableWidget(fuelGauge);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderTooltip(graphics, mouseX, mouseY);

        if (rocket == null) {
            return;
        }

        fuelGauge.update(rocket.rocketComponent.getFuel());

        fuelGauge.renderTooltip(graphics, mouseX, mouseY, font);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }
}
