package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTank;
import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
import com.st0x0ef.stellaris.common.menus.WaterSeparatorMenu;
import com.st0x0ef.stellaris.platform.systems.energy.EnergyContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class WaterSeparatorScreen extends AbstractContainerScreen<WaterSeparatorMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/water_separator.png");

    private final WaterSeparatorBlockEntity blockEntity = getMenu().getBlockEntity();
    private Gauge ingredientTankGauge;
    private Gauge resultTank1Gauge;
    private Gauge resultTank2Gauge;
    private Gauge.SidewayGauge energyGauge;

    public WaterSeparatorScreen(WaterSeparatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 177;
        imageHeight = 224;
        inventoryLabelY = imageHeight - 92;
    }

    @Override
    protected void init() {
        super.init();

        if (blockEntity == null) {
            return;
        }

        FluidTank ingredientTank = blockEntity.getIngredientTank();
        ingredientTankGauge = new Gauge(leftPos + 50, topPos + 58, 76, 42, Component.translatable("stellaris.screen.water"), GUISprites.WATER_OVERLAY, GUISprites.WATER_SEPARATOR_OVERLAY, (int)ingredientTank.getAmount(), (int)ingredientTank.getMaxCapacity()-1);
        addRenderableWidget(ingredientTankGauge);

        FluidTank resultTank1 = blockEntity.getResultTanks().getFirst();
        resultTank1Gauge = new Gauge(leftPos + 22, topPos + 54, 12, 46, Component.translatable("stellaris.screen.hydrogen"), GUISprites.HYDROGEN_OVERLAY, GUISprites.LIQUID_TANK_OVERLAY, (int)resultTank1.getAmount(), (int)resultTank1.getMaxCapacity()-1);
        addRenderableWidget(resultTank1Gauge);

        FluidTank resultTank2 = blockEntity.getResultTanks().get(1);
        resultTank2Gauge = new Gauge(leftPos + 142, topPos + 54, 12, 46, Component.translatable("stellaris.screen.oxygen"), GUISprites.OXYGEN_OVERLAY, GUISprites.LIQUID_TANK_OVERLAY, (int)resultTank2.getAmount(), (int)resultTank2.getMaxCapacity()-1);
        addRenderableWidget(resultTank2Gauge);

        EnergyContainer energyContainer = blockEntity.getWrappedEnergyContainer();
        energyGauge = new Gauge.SidewayGauge(leftPos + 64, topPos + 24, 47, 13, Component.translatable("stellaris.screen.energy"), GUISprites.SIDEWAYS_ENERGY_FULL, GUISprites.SIDEWAYS_ENERGY_OVERLAY, (int)energyContainer.getStoredEnergy(), (int)energyContainer.getMaxCapacity());
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

        ingredientTankGauge.update((int)blockEntity.getIngredientTank().getAmount());
        resultTank1Gauge.update((int)blockEntity.getResultTanks().getFirst().getAmount());
        resultTank2Gauge.update((int)blockEntity.getResultTanks().getLast().getAmount());
        energyGauge.update((int)blockEntity.getWrappedEnergyContainer().getStoredEnergy());
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
        resultTank1Gauge.renderTooltip(guiGraphics, x, y, this.font);
        resultTank2Gauge.renderTooltip(guiGraphics, x, y, this.font);
        energyGauge.renderTooltip(guiGraphics, x, y, this.font);
    }
}
