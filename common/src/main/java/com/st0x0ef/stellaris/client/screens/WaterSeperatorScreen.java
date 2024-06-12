package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTank;
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
public class WaterSeperatorScreen extends AbstractContainerScreen<WaterSeparatorMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/water_separator.png");
    ResourceLocation liquid_tank_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/water_tank_overlay.png");

    public WaterSeperatorScreen(WaterSeparatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 177;
        imageHeight = 224;
        this.inventoryLabelY = this.imageHeight - 92;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        renderWaterGauge();
        renderHydrogenGauge();
        renderOxygen();
        renderEnergyGauge();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    private void renderWaterGauge() {
        ResourceLocation water_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/fuel_overlay.png");
        ResourceLocation fluid_tank_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/water_separator_overlay.png");

        FluidTank waterTank = this.getMenu().getBlockEntity().getIngredientTank();
        Gauge waterGauge = new Gauge( this.leftPos + 50,  this.topPos + 58, 76, 40, null, water_overlay, fluid_tank_overlay, waterTank.getAmount(), waterTank.getMaxCapacity());

        this.addRenderableWidget(waterGauge);
    }

    private void renderHydrogenGauge() {
        ResourceLocation hydrogen_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/fuel_overlay.png");

        FluidTank hydrogenTank = this.getMenu().getBlockEntity().getResultTanks().getFirst();
        Gauge hydrogenGauge = new Gauge( this.leftPos + 22,  this.topPos + 52, 12, 46, null, hydrogen_overlay, liquid_tank_overlay, hydrogenTank.getAmount(), hydrogenTank.getMaxCapacity());

        this.addRenderableWidget(hydrogenGauge);
    }

    private void renderOxygen() {
        ResourceLocation oxygen_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/fuel_overlay.png");

        FluidTank waterTank = this.getMenu().getBlockEntity().getResultTanks().get(1);
        Gauge oxygenGauge = new Gauge( this.leftPos + 142,  this.topPos + 52, 12, 46, null, oxygen_overlay, liquid_tank_overlay, waterTank.getAmount(), waterTank.getMaxCapacity());

        this.addRenderableWidget(oxygenGauge);
    }

    private void renderEnergyGauge() {
        ResourceLocation water_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/sideway_energy_full.png");
        ResourceLocation fluid_tank_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/sideway_battery_overlay.png");

        EnergyContainer energyContainer = this.getMenu().getBlockEntity().getWrappedEnergyContainer().container();

        Gauge.SidewayGauge oxygenGauge = new Gauge.SidewayGauge( this.leftPos + 64,  this.topPos + 24, 47, 13, null, water_overlay, fluid_tank_overlay, (int) energyContainer.getStoredEnergy(),(int) energyContainer.getMaxCapacity());

        this.addRenderableWidget(oxygenGauge);
    }
}
