package com.st0x0ef.stellaris.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.client.skys.PlanetSky;
import com.st0x0ef.stellaris.client.skys.record.SkyPropertiesData;
import com.st0x0ef.stellaris.mixin.client.LevelRendererAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class FabricSkyRenderer {
    public static void registerPlanetSkyRenderer(WorldRenderContext worldRenderContext) {
        ResourceKey<Level> location = worldRenderContext.world().dimension();

        if (SkyPropertiesData.SKY_PROPERTIES.containsKey(location)) {
            PlanetSky planetSky = SkyPropertiesData.SKY_PROPERTIES.get(location);

            DimensionRenderingRegistry.registerDimensionEffects(location.location(), planetSky);

            if (!planetSky.getProperties().cloud()) {
                DimensionRenderingRegistry.registerCloudRenderer(location, context -> planetSky.getRenderer().removeCloud());
            }

            if (planetSky.getProperties().weather().isEmpty()) {
                DimensionRenderingRegistry.registerWeatherRenderer(location, context -> planetSky.getRenderer().removeSnowAndRain());
            }

            PoseStack poseStack = worldRenderContext.matrixStack(); // For an unknow reason, this is only not null if it's outside the registerSkyRenderer method. So this is the only workaround I've found.
            DimensionRenderingRegistry.registerSkyRenderer(location, context -> planetSky.renderSky(
                    worldRenderContext.world(),
                    poseStack,
                    worldRenderContext.projectionMatrix(),
                    ((LevelRendererAccessor) Minecraft.getInstance().levelRenderer).stellaris$ticks()
            ));
        }
    }
}
