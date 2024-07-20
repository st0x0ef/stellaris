package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.blocks.entities.machines.SolarPanelEntity;
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

    public static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/solar_panel.png");

    private final SolarPanelEntity blockEntity = getMenu().getBlockEntity();
    private Gauge energyGauge;

    public SolarPanelScreen(SolarPanelMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        imageWidth = 177;
        imageHeight = 228;
        inventoryLabelY = imageHeight - 92;
        titleLabelY += 40;
    }

    @Override
    protected void init() {
        super.init();

        if (blockEntity == null) {
            return;
        }

        energyGauge = new Gauge(leftPos + 108, topPos + 69, 13, 47, Component.translatable("stellaris.screen.energy"), GUISprites.ENERGY_FULL, null, (int) menu.getEnergyContainer().getStoredAmount(), (int) menu.getEnergyContainer().getCapacity());
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

        energyGauge.update(blockEntity.getEnergy(null).getStoredAmount());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("gauge_text.stellaris.max_generation", getMenu().getBlockEntity().getEnergyGeneratedPT()));
        energyGauge.renderTooltips(guiGraphics, x, y, font, components);
    }
}
