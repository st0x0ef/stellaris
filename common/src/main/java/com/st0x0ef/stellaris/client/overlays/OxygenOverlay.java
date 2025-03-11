package com.st0x0ef.stellaris.client.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class OxygenOverlay {
    public static ResourceLocation OXYGEN_OVERLAY = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "oxygenoverlay");
   public void render(GuiGraphics graphic, ResourceLocation texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, float alpha) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        graphic.setColor(1.0F, 1.0F, 1.0F, alpha);
        graphic.blit(OXYGEN_OVERLAY, 0, 0, -90, 0.0F, 0.0F, graphic.guiWidth(), graphic.guiHeight(), graphic.guiWidth(), graphic.guiHeight());
        RenderSystem.disableBlend();
        graphic.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }


    /**
     * will be rewrite to improve smooth texture render
     */

}




