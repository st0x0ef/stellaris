package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.blocks.entities.machines.CoalGeneratorEntity;
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

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class OxygenDistributorScreen extends AbstractContainerScreen<OxygenDistributorMenu> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/oxygen_distributor.png");
    public static final ResourceLocation BATTERY_OVERLAY = new ResourceLocation(Stellaris.MODID, "textures/gui/util/battery_overlay.png");
    public static final ResourceLocation ENERGY_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/energy_full.png");

    public OxygenDistributorScreen(OxygenDistributorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 177;
        imageHeight = 220;
        inventoryLabelY = imageHeight - 92;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics,mouseX,mouseY,partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);

        OxygenDistributorBlockEntity blockEntity = this.getMenu().getBlockEntity();
        if(blockEntity != null)
        {
            WrappedBlockEnergyContainer energyStorage = blockEntity.getWrappedEnergyContainer();

            Gauge gauge = new Gauge(this.leftPos + 147, this.topPos + 53, 13, 49, Component.translatable("stellaris.screen.energy"), ENERGY_TEXTURE, BATTERY_OVERLAY, (int) energyStorage.getStoredEnergy(), (int) energyStorage.getMaxCapacity());
            this.addRenderableWidget(gauge);
            gauge.renderTooltip(graphics, mouseX, mouseY, this.font);
        }

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }
}
