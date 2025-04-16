package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.client.screens.components.GaugeWidget;
import com.st0x0ef.stellaris.common.blocks.entities.machines.VacuumatorBlockEntity;
import com.st0x0ef.stellaris.common.menus.VacuumatorMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.st0x0ef.stellaris.Stellaris.id;

@Environment(EnvType.CLIENT)
public class VacumatorScreen extends AbstractContainerScreen<VacuumatorMenu> {
	public static final ResourceLocation texture = id("textures/gui/vacuumator.png");

	private final VacuumatorBlockEntity blockEntity = getMenu().getBlockEntity();
	private GaugeWidget energyGauge;

	public VacumatorScreen(VacuumatorMenu abstractContainerMenu, Inventory inventory, Component component) {
		super(abstractContainerMenu, inventory, component);
		this.imageWidth = 180;
		this.imageHeight = 188;
		this.inventoryLabelY = this.imageHeight - 95;
	}

	@Override
	protected void init() {
		super.init();

		if (blockEntity == null) {
			return;
		}

		energyGauge = new GaugeWidget(leftPos + 67, topPos + 15, 46, 15, Component.translatable("stellaris.screen.energyContainer"),
				GUISprites.SIDEWAYS_ENERGY_FULL, GUISprites.SIDEWAYS_BATTERY_OVERLAY, blockEntity.getEnergy(null).getMaxEnergy(), GaugeWidget.Direction4.LEFT_RIGHT);
		addRenderableWidget(energyGauge);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics,mouseX,mouseY,partialTicks);
		super.render(graphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(graphics, mouseX, mouseY);

		if (blockEntity == null) {
			return;
		}

		energyGauge.updateAmount(blockEntity.getEnergy(null).getEnergy());
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float var2,int var3, int var4) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, texture);
		graphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}

	@Override
	protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
		super.renderTooltip(guiGraphics, x, y);
		energyGauge.renderTooltip(guiGraphics, x, y, font);
	}
}
