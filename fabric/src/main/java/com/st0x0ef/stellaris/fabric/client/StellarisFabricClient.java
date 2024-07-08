package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.mixin.client.LevelRendererAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class StellarisFabricClient  {

    public void init() {
        StellarisClient.initClient();
        StellarisClient.registerEntityRenderer();
        StellarisClient.registerEntityModelLayer();

        WorldRenderEvents.LAST.register(FabricSkyRenderer::renderSky);
    }

    public static void registerDimension(Map<ResourceKey<Level>, SkyRenderer> renderer) {
        renderer.forEach( (levelResourceKey, skyRenderer) -> {
            DimensionRenderingRegistry.registerDimensionEffects(levelResourceKey.location(), skyRenderer);
            DimensionRenderingRegistry.registerCloudRenderer(levelResourceKey, context -> {
                Vec3 camera = context.camera().getPosition();
                int ticks = ((LevelRendererAccessor) Minecraft.getInstance().levelRenderer).stellaris$ticks();
                skyRenderer.renderClouds();
            });


            DimensionRenderingRegistry.registerSkyRenderer(levelResourceKey, context -> {

                        skyRenderer.render(
                                context.world(),
                                ((LevelRendererAccessor) Minecraft.getInstance().levelRenderer).stellaris$ticks(),
                                context.tickDelta(),
                                context.matrixStack(),
                                context.camera(),
                                context.projectionMatrix(),
                                false,
                                () -> {
                                });
                    }
            );
        });





    }
}
