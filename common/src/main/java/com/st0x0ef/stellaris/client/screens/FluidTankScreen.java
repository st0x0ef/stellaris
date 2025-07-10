package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.client.screens.components.FluidGaugeWidget;
import com.st0x0ef.stellaris.client.screens.components.GaugeWidget;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankBlockEntity;
import com.st0x0ef.stellaris.common.menus.FluidTankMenu;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.SingleFluidStorage;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FluidTankScreen extends AbstractContainerScreen<FluidTankMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocationUtils.guiTexture("fluid_tank");

    private final FluidTankBlockEntity blockEntity = getMenu().getBlockEntity();
    private FluidGaugeWidget fluidGauge;

    public FluidTankScreen(FluidTankMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        imageWidth = 180;
        imageHeight = 188;

        titleLabelX = (180 - Minecraft.getInstance().font.width(title.getString())) / 2;
        titleLabelY = 2;
    }

    @Override
    protected void init() {
        super.init();

        if (blockEntity == null) {
            return;
        }

        Component tooltip = blockEntity.getFluidTank().isEmpty() ?
                Component.translatable("stellaris.screen.empty_fluid") :
                blockEntity.getFluidTank().getFluidInTank(0).getName();

        fluidGauge = new FluidGaugeWidget(leftPos + 84, topPos + 36, 12, 46, tooltip, blockEntity::getFluidTank, 0, GaugeWidget.Direction4.DOWN_UP)
                .setOverlaySprite(GUISprites.FLUID_TANK_OVERLAY);
        addRenderableWidget(fluidGauge);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        Component tooltip = blockEntity.getFluidTank().isEmpty() ?
                Component.translatable("stellaris.screen.empty_fluid") :
                blockEntity.getFluidTank().getFluidInTank(0).getName();
        fluidGauge.setMessage(tooltip);
        fluidGauge.renderTooltip(guiGraphics, x, y, font);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 5726575, false);
    }
}
