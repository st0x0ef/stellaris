package com.st0x0ef.stellaris.client.renderers.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.systems.RenderSystem;

public class TextureRenderer {
    private ResourceLocation resourceLocation;

    public void loadTexture(String texturePath) {
        this.resourceLocation = new ResourceLocation(texturePath);
    }

    public void renderTexture(GuiGraphics graphics, int x, int y, int width, int height) {
        if (this.resourceLocation != null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, this.resourceLocation);
            graphics.blit(this.resourceLocation, x, y, 0, 0, width, height, width, height);
        }
    }
}
