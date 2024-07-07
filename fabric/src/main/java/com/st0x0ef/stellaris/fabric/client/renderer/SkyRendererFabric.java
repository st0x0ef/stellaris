package com.st0x0ef.stellaris.fabric.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.skys.record.SkyPropertiesData;
import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.mixin.client.LevelRendererAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class SkyRendererFabric {
    public static int getTicks() {
        return ((LevelRendererAccessor) Minecraft.getInstance().levelRenderer).getTicks();
    }

    public static void RegisterSkyRenderer(WorldRenderContext context) {
        LocalPlayer p = Minecraft.getInstance().player;

        if (p == null || SkyPropertiesData.getSkyPropertiesById(p.level().dimension()) == null) {
            if (!SkyPropertiesData.SKY_PROPERTIES.isEmpty() && SkyPropertiesData.SKY_PROPERTIES.get(0) != null) {
                Stellaris.LOG.info(SkyPropertiesData.SKY_PROPERTIES.get(0).sunriseColor());
            } else {
                Stellaris.LOG.warn("SY_PROPERTIES is empty or contains null elements");
            }
            return;
        }

        ResourceKey<Level> dim = p.level().dimension();
        Stellaris.LOG.info(dim.toString());

        PoseStack matrixStack = context.matrixStack();
        if (matrixStack == null) {
            Stellaris.LOG.error("PoseStack is null in RegisterSkyRenderer method");
            return;
        }

//        SkyRenderer.render(p.clientLevel, context.tickDelta(), matrixStack, context.camera(), context.projectionMatrix(), false, () -> {});
    }
}
