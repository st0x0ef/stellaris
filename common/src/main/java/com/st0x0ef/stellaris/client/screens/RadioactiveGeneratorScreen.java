package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.blocks.entities.machines.CoalGeneratorEntity;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.menus.RadioactiveGeneratorMenu;
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
public class RadioactiveGeneratorScreen extends AbstractContainerScreen<RadioactiveGeneratorMenu> {
	public static final ResourceLocation texture = new ResourceLocation(Stellaris.MODID, "textures/gui/radioactive_generator.png");
	public static final ResourceLocation BATTERY_OVERLAY = new ResourceLocation(Stellaris.MODID, "textures/gui/util/battery_overlay.png");
	public static final ResourceLocation ENERGY_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/energy_full.png");

	public RadioactiveGeneratorScreen(RadioactiveGeneratorMenu abstractContainerMenu, Inventory inventory, Component component) {
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

			Gauge gauge = new Gauge(this.leftPos + 147, this.topPos + 51, 13, 49, Component.translatable("stellaris.screen.energy"), ENERGY_TEXTURE, BATTERY_OVERLAY, (int) energyStorage.getStoredEnergy(), (int) energyStorage.getMaxCapacity());
			this.addRenderableWidget(gauge);
			List<Component> components = new ArrayList<>();
			components.add(Component.translatable("gauge_text.stellaris.max_generation", blockEntity.getEnergyGeneratedPT()));
			gauge.renderTooltips(graphics, mouseX, mouseY, this.font, components);
		}

	}

	@Override
	protected void renderBg(GuiGraphics graphics, float var2,int var3, int var4) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, texture);
		graphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}

}
