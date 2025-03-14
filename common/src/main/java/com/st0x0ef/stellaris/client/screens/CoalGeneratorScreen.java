package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.GaugeWidget;
import com.st0x0ef.stellaris.common.blocks.entities.machines.CoalGeneratorEntity;
import com.st0x0ef.stellaris.common.menus.CoalGeneratorMenu;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class CoalGeneratorScreen extends AbstractContainerScreen<CoalGeneratorMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/coal_generator.png");

    private final CoalGeneratorEntity blockEntity = getMenu().getBlockEntity();
    private GaugeWidget energyGauge;

    public CoalGeneratorScreen(CoalGeneratorMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);

        imageWidth = 180;
        imageHeight = 188;

        titleLabelX = (180 - Minecraft.getInstance().font.width(title.getString())) / 2;
        titleLabelY = 4;

        inventoryLabelY = imageHeight - 92;
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
        renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderTooltip(graphics, mouseX, mouseY);

        if (blockEntity == null) {
            return;
        }

        energyGauge.updateAmount(blockEntity.getEnergy(null).getEnergy());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

        if (menu.isLit()) {
            int i = Mth.ceil(menu.getLitProgress() * 13.0F) + 1;
            graphics.blitSprite(GUISprites.COAL_GENERATOR_LIT_PROGRESS_SPRITE, 8, 13, 0, 14 - i, leftPos + 86, topPos + 57 + 13 - i, 14, i);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        energyGauge.renderTooltips(guiGraphics, x, y, font, list ->
                list.add(Component.translatable("gauge_text.stellaris.max_generation", blockEntity.getEnergyGeneratedPT())));
    }
}
