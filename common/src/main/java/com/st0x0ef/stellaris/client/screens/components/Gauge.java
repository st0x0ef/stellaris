package com.st0x0ef.stellaris.client.screens.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.helper.ScreenHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class Gauge extends AbstractWidget {
    private ResourceLocation buttonTexture;

    private int xTexStart;
    private int yTexStart;

    private int yDiffText;

    private int textureWidth;
    private int textureHeight;

    public Gauge(int x, int y, int width, int height, Component message, ResourceLocation texture) {
        super(x, y, width, height, message);
        this.yDiffText = 0;
        this.xTexStart = 0;
        this.yTexStart = 0;
        this.buttonTexture = texture;

    }


    @SuppressWarnings("unchecked")
    private <T extends Gauge> T cast() {
        return (T) this;
    }


    public <T extends Gauge> T size(int texWidth, int texHeight) {
        this.textureWidth = texWidth;
        this.textureHeight = texHeight;
        return cast();
    }

    public <T extends Gauge> T setUVs(int xTexStart, int yTexStart) {
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        return cast();
    }

    public void setYShift(int y) {
        this.yDiffText = y;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableDepthTest();

        int i = this.yTexStart;
        if (this.isHoveredOrFocused()) {
            i += this.yDiffText;
        }

        /** TEXTURE MANAGER */
        ResourceLocation texture = this.buttonTexture;

        /** TEXTURE RENDERER */
        RenderSystem.setShaderTexture(0, texture);
        ScreenHelper.drawTexture(this.getX(), this.getY(), this.width, this.height, this.buttonTexture, false);


        /** FONT RENDERER */
        Font fontRenderer = minecraft.font;
//        int j = getFGColor();
//
//        graphics.drawCenteredString(fontRenderer, this.getMessage(), this.getX() + this.width / 2,
//                this.getY() + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }


}
