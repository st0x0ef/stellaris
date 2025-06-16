package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.client.screens.components.GaugeWidget;
import com.st0x0ef.stellaris.common.blocks.entities.machines.PumpjackBlockEntity;
import com.st0x0ef.stellaris.common.menus.PumpjackMenu;
import com.st0x0ef.stellaris.common.oil.OilUtils;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import com.st0x0ef.stellaris.common.utils.Utils;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.SingleFluidStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PumpjackScreen extends AbstractContainerScreen<PumpjackMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocationUtils.guiTexture("pumpjack");

    private final PumpjackBlockEntity blockEntity = getMenu().getBlockEntity();
    private GaugeWidget resultTankGauge;
    private GaugeWidget energyGauge;

    public PumpjackScreen(PumpjackMenu menu, Inventory playerInventory, Component title) {
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

        SingleFluidStorage resultTank = blockEntity.getResultTank();
        resultTankGauge = new GaugeWidget(leftPos + 114, topPos + 46, 12, 46, Component.translatable("stellaris.screen.oil"), GUISprites.OIL_OVERLAY, GUISprites.FLUID_TANK_OVERLAY, resultTank.getTankCapacity(0), GaugeWidget.Direction4.DOWN_UP);
        addRenderableWidget(resultTankGauge);

        energyGauge = new GaugeWidget(leftPos + 68, topPos + 20, 44, 6, Component.translatable("stellaris.screen.energyContainer"), GUISprites.SIDEWAYS_ENERGY_FULL, null, blockEntity.getEnergy(null).getMaxEnergy(), GaugeWidget.Direction4.LEFT_RIGHT);
        addRenderableWidget(energyGauge);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        if (blockEntity == null || Minecraft.getInstance().level == null) {
            return;
        }

        guiGraphics.drawString(this.font, "Oil Level", leftPos + 19, topPos + 40, Utils.getColorHexCode("gray"));
        guiGraphics.drawCenteredString(this.font, String.valueOf(blockEntity.chunkOilLevel(Minecraft.getInstance().level)), leftPos + 40, topPos + 51, OilUtils.getOilLevelColor(blockEntity.chunkOilLevel(Minecraft.getInstance().level)));

        resultTankGauge.updateAmount(blockEntity.getResultTank().getFluidValueInTank());
        energyGauge.updateAmount(blockEntity.getEnergy(null).getEnergy());
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        resultTankGauge.renderTooltip(guiGraphics, x, y, font);
        energyGauge.renderTooltip(guiGraphics, x, y, font);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 5726575, false);
    }
}