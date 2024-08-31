package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTank;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FuelRefineryBlockEntity;
import com.st0x0ef.stellaris.common.menus.FuelRefineryMenu;
import com.st0x0ef.stellaris.platform.systems.energy.EnergyContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FuelRefineryScreen extends AbstractContainerScreen<FuelRefineryMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/fuel_refinery.png");

    private final FuelRefineryBlockEntity blockEntity = getMenu().getBlockEntity();
    private Gauge ingredientTankGauge;
    private Gauge resultTankGauge;
    private Gauge energyGauge;

    public FuelRefineryScreen(FuelRefineryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 177;
        imageHeight = 184;
        inventoryLabelY = imageHeight - 92;
    }

    @Override
    protected void init() {
        super.init();

        if (blockEntity == null) {
            return;
        }

        FluidTank ingredientTank = blockEntity.getIngredientTank();
        ingredientTankGauge = new Gauge(leftPos + 43, topPos + 22, 12, 46, Component.translatable("stellaris.screen.oil"), GUISprites.OIL_OVERLAY, GUISprites.LIQUID_TANK_OVERLAY, (int)ingredientTank.getAmount(), (int)ingredientTank.getMaxCapacity() -1);
        addRenderableWidget(ingredientTankGauge);

        FluidTank resultTank = blockEntity.getResultTank();
        resultTankGauge = new Gauge(leftPos + 100, topPos + 22, 12, 46, Component.translatable("stellaris.screen.fuel"), GUISprites.FUEL_OVERLAY, GUISprites.LIQUID_TANK_OVERLAY, (int)resultTank.getAmount(), (int)resultTank.getMaxCapacity() -1);
        addRenderableWidget(resultTankGauge);

        EnergyContainer energyContainer = blockEntity.getWrappedEnergyContainer().container();
        energyGauge = new Gauge(leftPos + 150, topPos + 21, 13, 47, Component.translatable("stellaris.screen.energy"), GUISprites.ENERGY_FULL, GUISprites.BATTERY_OVERLAY, (int) energyContainer.getStoredEnergy(), (int) energyContainer.getMaxCapacity());
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
        resultTankGauge.update((int)blockEntity.getResultTank().getAmount());
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
        ingredientTankGauge.renderTooltip(guiGraphics, x, y, font);
        resultTankGauge.renderTooltip(guiGraphics, x, y, font);
        energyGauge.renderTooltip(guiGraphics, x, y, font);
    }
}
