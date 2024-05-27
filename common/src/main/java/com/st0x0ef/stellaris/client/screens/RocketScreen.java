package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.helper.ScreenHelper;
import com.st0x0ef.stellaris.common.menus.RocketMenu;
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

    public int RocketFuel = 0;
    public int MaxFuel = this.getMenu().getRocket().MAX_FUEL;

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

        RocketFuel = this.getMenu().getRocket().getFuel();

        int fluidtankX = this.leftPos + 51;
        int fluidtankY = this.topPos + 27;
        int fluidtankWidth = 12;
        int fluidtankHeight = 46;

        if (RocketFuel > 10000) {
            RocketFuel = MaxFuel;
            graphics.blit(fuel_overlay, fluidtankX, fluidtankY, fluidtankWidth, fluidtankHeight - 1, fluidtankWidth, fluidtankHeight -1, fluidtankWidth, fluidtankHeight - 1);
        } else {
            graphics.blit(fuel_overlay, fluidtankX, fluidtankY + 45 - (int) (RocketFuel / (MaxFuel / 10) * 4.5), (float) fluidtankWidth, (float) (fluidtankHeight - (MaxFuel / 1000 - RocketFuel / (MaxFuel / 10)) * 4.5), fluidtankWidth, (int) (fluidtankHeight - (MaxFuel / 1000 - RocketFuel / (MaxFuel / 10)) * 4.6), fluidtankWidth, 45);
        }

        ScreenHelper.drawTexture(fluidtankX, fluidtankY, fluidtankWidth, fluidtankHeight, fluid_tank_overlay, false);

        if (mouseX >= fluidtankX && mouseX <= fluidtankX + fluidtankWidth && mouseY >= fluidtankY && mouseY <= fluidtankY + fluidtankHeight) {
            graphics.renderTooltip(this.font, Component.literal(Fuel.getString() + " : " + RocketFuel), mouseX, mouseY);
        }

/*     Gauge gauge = new Gauge(this.leftPos + 51, this.topPos + 27, 12, getPourcentTexture(), Component.nullToEmpty(""), new ResourceLocation(Stellaris.MODID, "textures/block/fluids/fuel_overlay.png"))
               .setUVs(20, 30);

        this.addRenderableWidget(gauge); */
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }


    public int getPourcentTexture() {
        return this.getMenu().getRocket().getFuel() * 47 / 10000;
    }
}
