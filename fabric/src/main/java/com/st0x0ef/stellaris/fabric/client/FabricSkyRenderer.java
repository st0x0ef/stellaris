package com.st0x0ef.stellaris.fabric.client;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.skys.record.SkyPropertiesData;
import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.mixin.client.LevelRendererAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class FabricSkyRenderer {

    public static void renderSky(WorldRenderContext context) {
        ResourceKey<Level> location =  context.world().dimension();

        if (SkyPropertiesData.SKY_PROPERTIES.containsKey(location)) {
            SkyRenderer skyRenderer = SkyPropertiesData.SKY_PROPERTIES.get(location);
            int ticks = ((LevelRendererAccessor) Minecraft.getInstance().levelRenderer).stellaris$ticks();
            skyRenderer.render(
                    context.world(),
                    ticks,
                    context.tickDelta(),
                    context.matrixStack(),
                    context.camera(),
                    context.projectionMatrix(),
                    false,
                    () -> {});
        }

    }
}
