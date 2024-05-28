package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.Gauge;
import com.st0x0ef.stellaris.common.entities.RocketEntity;
import com.st0x0ef.stellaris.common.menus.RocketMenu;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class RocketScreen extends AbstractContainerScreen<RocketMenu> {

    public static final ResourceLocation texture = new ResourceLocation(Stellaris.MODID, "textures/gui/rocket.png");

    public static final ResourceLocation fuel_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/fuel_overlay.png");
    public static final ResourceLocation fluid_tank_overlay = new ResourceLocation(Stellaris.MODID, "textures/gui/util/fluid_tank_overlay.png");

    public int rocket_fuel = 0;
    public int max_fuel = RocketEntity.MAX_FUEL;
    public Component capacity;

    public static final Component Fuel = Component.translatable("text.stellaris.rocketscreen.fuel");


    public RocketScreen(RocketMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 177;
        this.imageHeight = 177;
        this.inventoryLabelY = this.imageHeight - 92;

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);

        rocket_fuel = this.getMenu().getRocket().getFuel();
        String GaugeComponent = Fuel.getString() + " : " + rocket_fuel + " / " + max_fuel;

        Gauge gauge = new Gauge(this.leftPos + 51, this.topPos + 27, 12, 46, Fuel, fuel_overlay, rocket_fuel, max_fuel);

        if (rocket_fuel >= max_fuel) {
            capacity = Utils.getMessageComponent(GaugeComponent, "Lime");
        } else if (rocket_fuel <= 0) {
            capacity = Utils.getMessageComponent(GaugeComponent, "Red");
        } else {
            capacity = Utils.getMessageComponent(GaugeComponent, "Orange");
        }

        if (mouseX >= this.leftPos + 51 && mouseX <= this.leftPos + 51 + 12 && mouseY >= this.topPos + 27 && mouseY <= this.topPos + 27 + 46) {
            graphics.renderTooltip(this.font, capacity, mouseX, mouseY);
        }

        this.addRenderableWidget(gauge);


    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }



}
