package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenDistributorBlockEntity;
import com.st0x0ef.stellaris.common.menus.OxygenDistributorMenu;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class OxygenDistributorScreen extends AbstractContainerScreen<OxygenDistributorMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/oxygen_distributor.png");

    private final OxygenDistributorBlockEntity blockEntity = getMenu().getBlockEntity();
    private Gauge energyGauge;

    public OxygenDistributorScreen(OxygenDistributorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 177;
        imageHeight = 220;
        inventoryLabelY = imageHeight - 92;
    }

    @Override
    protected void init() {
        super.init();

        if (blockEntity == null) return;

        WrappedBlockEnergyContainer energyContainer = blockEntity.getWrappedEnergyContainer();
        energyGauge = new Gauge(leftPos + 147, topPos + 55, 13, 48, Component.translatable("stellaris.screen.energy"), GUISprites.ENERGY_FULL, GUISprites.BATTERY_OVERLAY, (int) energyContainer.getStoredEnergy(), (int) energyContainer.getMaxCapacity());
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

        energyGauge.update(blockEntity.getWrappedEnergyContainer().getStoredEnergy());
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        energyGauge.renderTooltip(guiGraphics, x, y, this.font);
    }
}
