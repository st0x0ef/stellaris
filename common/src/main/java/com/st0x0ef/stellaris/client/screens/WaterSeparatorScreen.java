package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.client.screens.components.GaugeChunkWidget;
import com.st0x0ef.stellaris.client.screens.components.GaugeWidget;
import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
import com.st0x0ef.stellaris.common.menus.WaterSeparatorMenu;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.FluidStorage;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.SingleFluidStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class WaterSeparatorScreen extends AbstractContainerScreen<WaterSeparatorMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocationUtils.guiTexture("water_separator");

    private final WaterSeparatorBlockEntity blockEntity = getMenu().getBlockEntity();
    private GaugeChunkWidget ingredientTankGauge;
    private GaugeWidget hydrogenTankGauge;
    private GaugeWidget oxygenTankGauge;
    private GaugeWidget energyGauge;

    public WaterSeparatorScreen(WaterSeparatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 180;
        imageHeight = 224;

        titleLabelX = (180 - Minecraft.getInstance().font.width(title.getString())) / 2;
        titleLabelY = 2;
    }

    @Override
    protected void init() {
        super.init();

        if (blockEntity == null) {
            return;
        }

        SingleFluidStorage ingredientTank = blockEntity.getIngredientTank();
        ingredientTankGauge = new GaugeChunkWidget(leftPos + 53, topPos + 54, 76, 46, Component.translatable("stellaris.screen.water"), GUISprites.WATER_OVERLAY, GUISprites.WATER_SEPARATOR_OVERLAY, ingredientTank.getTankCapacity(0), GaugeChunkWidget.Direction4.DOWN_UP);
        addRenderableWidget(ingredientTankGauge);

        FluidStorage resultTanks = blockEntity.getResultTanks();
        hydrogenTankGauge = new GaugeWidget(leftPos + 22, topPos + 54, 12, 46, Component.translatable("stellaris.screen.hydrogen"), GUISprites.HYDROGEN_OVERLAY, GUISprites.FLUID_TANK_OVERLAY, resultTanks.getTankCapacity(WaterSeparatorBlockEntity.HYDROGEN_TANK), GaugeWidget.Direction4.UP_DOWN);
        addRenderableWidget(hydrogenTankGauge);

        oxygenTankGauge = new GaugeWidget(leftPos + 146, topPos + 54, 12, 46, Component.translatable("stellaris.screen.oxygen"), GUISprites.OXYGEN_OVERLAY, GUISprites.FLUID_TANK_OVERLAY, resultTanks.getTankCapacity(WaterSeparatorBlockEntity.OXYGEN_TANK), GaugeWidget.Direction4.UP_DOWN);
        addRenderableWidget(oxygenTankGauge);

        energyGauge = new GaugeWidget(leftPos + 68, topPos + 20, 44, 6, Component.translatable("stellaris.screen.energyContainer"), GUISprites.SIDEWAYS_ENERGY_FULL, null, blockEntity.getEnergy(null).getMaxEnergy(), GaugeWidget.Direction4.LEFT_RIGHT);
        addRenderableWidget(energyGauge);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        if (blockEntity == null) {
            return;
        }

        ingredientTankGauge.updateAmount((int) blockEntity.getIngredientTank().getFluidValueInTank());
        hydrogenTankGauge.updateAmount((int) blockEntity.getResultTanks().getFluidValueInTank(WaterSeparatorBlockEntity.HYDROGEN_TANK));
        oxygenTankGauge.updateAmount((int) blockEntity.getResultTanks().getFluidValueInTank(WaterSeparatorBlockEntity.OXYGEN_TANK));
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
        ingredientTankGauge.renderTooltip(guiGraphics, x, y, this.font);
        hydrogenTankGauge.renderTooltip(guiGraphics, x, y, this.font);
        oxygenTankGauge.renderTooltip(guiGraphics, x, y, this.font);
        energyGauge.renderTooltip(guiGraphics, x, y, this.font);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 5726575, false);
    }
}
