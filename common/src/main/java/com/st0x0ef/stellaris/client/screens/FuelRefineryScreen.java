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
    ResourceLocation liquid_tank_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/water_tank_overlay.png");

    public FuelRefineryScreen(FuelRefineryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 177;
        imageHeight = 184;
        inventoryLabelY = imageHeight - 92;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
        FuelRefineryBlockEntity blockEntity = this.getMenu().getBlockEntity();
        if (blockEntity != null){
            renderFuelGauge(blockEntity);
            renderOilGauge(blockEntity);
            renderEnergyGauge(blockEntity);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    private void renderOilGauge(FuelRefineryBlockEntity blockEntity) {
        ResourceLocation oil_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/oil_gui_overlay.png");

        FluidTank oilTank = blockEntity.getIngredientTank();
        Gauge oilGauge = new Gauge( this.leftPos + 22,  this.topPos + 54, 12, 46, null, oil_overlay, liquid_tank_overlay, oilTank.getAmount(), oilTank.getMaxCapacity());

        this.addRenderableWidget(oilGauge);
    }

    private void renderFuelGauge(FuelRefineryBlockEntity blockEntity) {
        ResourceLocation fuel_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/fuel_overlay.png");

        FluidTank fuelTank = blockEntity.getResultTank();
        Gauge fuelGauge = new Gauge( this.leftPos + 142,  this.topPos + 54, 12, 46, null, fuel_overlay, liquid_tank_overlay, fuelTank.getAmount(), fuelTank.getMaxCapacity());

        this.addRenderableWidget(fuelGauge);
    }

    private void renderEnergyGauge(FuelRefineryBlockEntity blockEntity) {
        ResourceLocation energy_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/energy_full.png");
        ResourceLocation battery_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/battery_overlay.png");

        EnergyContainer energyContainer = blockEntity.getWrappedEnergyContainer().container();

        Gauge energyGauge = new Gauge( this.leftPos + 140, this.topPos + 50, 13, 47, Component.translatable("stellaris.screen.energy"), energy_overlay, battery_overlay, (int) energyContainer.getStoredEnergy(),(int) energyContainer.getMaxCapacity());

        this.addRenderableWidget(energyGauge);
    }
}
