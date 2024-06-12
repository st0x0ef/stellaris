package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.blocks.entities.machines.CoalGeneratorEntity;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeTextHelper;
import com.st0x0ef.stellaris.common.blocks.machines.gauge.GaugeValueHelper;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.menus.CoalGeneratorMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class CoalGeneratorScreen extends AbstractContainerScreen<CoalGeneratorMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/coal_generator.png");
	private static final ResourceLocation LIT_PROGRESS_SPRITE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/fire_progress.png");
	public static final ResourceLocation FUEL_OVERLAY = new ResourceLocation(Stellaris.MODID, "textures/gui/util/battery_overlay.png");
	public static final ResourceLocation ENERGY_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/energy_full.png");

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

		CoalGeneratorEntity blockEntity = this.getMenu().getBlockEntity();
		if(blockEntity != null)
		{
			WrappedBlockEnergyContainer energyStorage = blockEntity.getWrappedEnergyContainer();

			Gauge gauge = new Gauge(this.leftPos + 147, this.topPos + 51, 13, 49, null, ENERGY_TEXTURE, FUEL_OVERLAY, (int) energyStorage.getStoredEnergy(), (int) energyStorage.getMaxCapacity());
			this.addRenderableWidget(gauge);

		}


	}

	@Override
	protected void renderBg(GuiGraphics graphics, float var2,int var3, int var4) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		int k = this.leftPos;
		int l = this.topPos;
		if (this.menu.isLit()) {
			int n = Mth.ceil(this.menu.getLitProgress() * 13.0F) + 1;
			graphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 14 - n, k + 84, l + 69 + 14 - n, 14, n);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int i, int j) {
		super.renderLabels(graphics,i,j);

		CoalGeneratorEntity blockEntity = this.getMenu().getBlockEntity();
		if(blockEntity != null)
		{
			WrappedBlockEnergyContainer energyStorage = blockEntity.getWrappedEnergyContainer();
			if(energyStorage!= null)
			{
				graphics.drawString(this.font, GaugeTextHelper.getStoredText(GaugeValueHelper.getEnergy(energyStorage.getStoredEnergy())).build(), this.titleLabelX, 128-30, 0x3C3C3C);
				graphics.drawString(this.font, GaugeTextHelper.getCapacityText(GaugeValueHelper.getEnergy(energyStorage.getMaxCapacity())).build(), this.titleLabelX, 140-30, 0x3C3C3C);
				graphics.drawString(this.font, GaugeTextHelper.getMaxGenerationPerTickText(GaugeValueHelper.getEnergy(blockEntity.getEnergyGeneratedPT())).build(), this.titleLabelX, 152-30, 0x3C3C3C);
			}
		}
	}
}
