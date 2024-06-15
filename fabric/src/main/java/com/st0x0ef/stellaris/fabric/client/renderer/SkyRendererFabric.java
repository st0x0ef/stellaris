package com.st0x0ef.stellaris.fabric.client.renderer;

import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class SkyRendererFabric {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final LocalPlayer p = mc.player;

    public static void RegisterSkyRenderer(WorldRenderContext context) {

        if (p == null || SkyRenderer.getRenderableType(p.level().dimension()) == null) return;

        ResourceKey<Level> dim = p.level().dimension();
        SkyRenderer.render(dim, context.tickDelta(), context.projectionMatrix(), context.matrixStack(), context.positionMatrix());
    }
}
