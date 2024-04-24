package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.machines.CoalGeneratorEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.SolarPanelEntity;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeTextHelper;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeValueHelper;
import com.st0x0ef.stellaris.common.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.menus.CoalGeneratorMenu;
import com.st0x0ef.stellaris.common.menus.SolarPanelMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class CoalGeneratorScreen extends AbstractContainerScreen<CoalGeneratorMenu> {
	public static final ResourceLocation texture = new ResourceLocation(Stellaris.MODID, "textures/gui/coal_generator.png");

	public CoalGeneratorScreen(CoalGeneratorMenu abstractContainerMenu, Inventory inventory, Component component) {
		super(abstractContainerMenu, inventory, component);
		this.imageWidth = 177;
		this.imageHeight = 228;
		this.inventoryLabelY = this.imageHeight - 92;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics,mouseX,mouseY,partialTicks);
		super.render(graphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float var2,int var3, int var4) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, texture);
		graphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int i, int j) {
		super.renderLabels(graphics,i,j);

		CoalGeneratorEntity blockEntity = this.getMenu().getBlockEntity();
		if(blockEntity != null)
		{
			WrappedBlockEnergyContainer energyStorage = blockEntity.getEnergyContainer();
			if(energyStorage!= null)
			{
				graphics.drawString(this.font, GaugeTextHelper.getStoredText(GaugeValueHelper.getEnergy(energyStorage.getStoredEnergy())).build(), this.titleLabelX, 128-30, 0x3C3C3C);
				graphics.drawString(this.font, GaugeTextHelper.getCapacityText(GaugeValueHelper.getEnergy(energyStorage.getMaxCapacity())).build(), this.titleLabelX, 140-30, 0x3C3C3C);
				graphics.drawString(this.font, GaugeTextHelper.getMaxGenerationPerTickText(GaugeValueHelper.getEnergy(blockEntity.getEnergyGeneratedPT())).build(), this.titleLabelX, 152-30, 0x3C3C3C);
			}

		}

	}
}
