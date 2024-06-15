package com.st0x0ef.stellaris.fabric.client.renderer;

import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import dev.architectury.event.events.client.ClientTickEvent;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class SkyRendererFabric {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final LocalPlayer p = mc.player;

    public static void RegisterSkyRenderer(WorldRenderContext context) {

        if (p == null || SkyRenderer.getRenderableType(p.level().dimension()) == null) return;

        ResourceKey<Level> dim = p.level().dimension();
        SkyRenderer.render(dim, context.tickDelta(), context.projectionMatrix(), context.positionMatrix(), context.matrixStack());
    }
}
