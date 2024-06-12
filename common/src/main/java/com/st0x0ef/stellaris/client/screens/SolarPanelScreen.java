package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.blocks.entities.machines.SolarPanelEntity;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.menus.SolarPanelMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SolarPanelScreen extends AbstractContainerScreen<SolarPanelMenu> {
	public static final ResourceLocation texture = new ResourceLocation(Stellaris.MODID, "textures/gui/solar_panel.png");

	public static final ResourceLocation fuel_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/energy_full.png");


	public SolarPanelScreen(SolarPanelMenu abstractContainerMenu, Inventory inventory, Component component) {
		super(abstractContainerMenu, inventory, component);
		this.imageWidth = 177;
		this.imageHeight = 228;
		this.inventoryLabelY = this.imageHeight - 92;
		this.titleLabelY += 40;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics,mouseX,mouseY,partialTicks);
		super.render(graphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(graphics, mouseX, mouseY);

		Gauge gauge = new Gauge(this.leftPos + 108, this.topPos + 69, 13, 47, Component.translatable("stellaris.screen.energy"), fuel_overlay, null, (int) this.menu.getEnergyContainer().getStoredEnergy(), (int) this.menu.getEnergyContainer().getMaxCapacity());
		this.addRenderableWidget(gauge);

		List<Component> components = new ArrayList<>();
		components.add(Component.translatable("gauge_text.stellaris.max_generation", this.getMenu().getBlockEntity().getEnergyGeneratedPT()));
		gauge.renderTooltips(graphics, mouseX, mouseY, this.font, components);

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

		SolarPanelEntity blockEntity = this.getMenu().getBlockEntity();
		if(blockEntity != null)
		{
			WrappedBlockEnergyContainer energyStorage = blockEntity.getWrappedEnergyContainer();
			if(energyStorage!= null)
			{
				graphics.drawString(this.font, Component.translatable("gauge_text.stellaris.stored", energyStorage.getStoredEnergy()), 25, 7, 0x3C3C3C);
				graphics.drawString(this.font, Component.translatable("gauge_text.stellaris.capacity", energyStorage.getMaxCapacity()), 25, 17, 0x3C3C3C);
			}

		}

	}
}
