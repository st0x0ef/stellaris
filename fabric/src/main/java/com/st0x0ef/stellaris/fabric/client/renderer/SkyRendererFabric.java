package com.st0x0ef.stellaris.fabric.client.renderer;

import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class SkyRendererFabric {
    public static void RegisterSkyRenderer(WorldRenderContext context) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p == null) {
            return;
        }
        ResourceKey<Level> dim = p.level().dimension();
        SkyRenderer.render(dim, context.tickDelta(), context.projectionMatrix(), context.matrixStack(), context.positionMatrix());
    }
}
