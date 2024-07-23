package com.st0x0ef.stellaris.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.skys.record.SkyPropertiesData;
import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.mixin.client.LevelRendererAccessor;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class FabricSkyRenderer {

    public static void renderSky(WorldRenderContext context) {
        ResourceKey<Level> location = context.world().dimension();

        if (SkyPropertiesData.SKY_PROPERTIES.containsKey(location)) {
            SkyRenderer skyRenderer = SkyPropertiesData.SKY_PROPERTIES.get(location);
            int ticks = ((LevelRendererAccessor) Minecraft.getInstance().levelRenderer).stellaris$ticks();

            PoseStack poseStack = context.matrixStack();
            if (poseStack != null) {
                skyRenderer.render(
                        context.world(),
                        ticks,
                        context.tickDelta(),
                        poseStack,
                        context.camera(),
                        context.projectionMatrix(),
                        false,
                        () -> {}
                );
            } else {
                Stellaris.LOG.error("PoseStack is null");
            }
        }
    }
}
