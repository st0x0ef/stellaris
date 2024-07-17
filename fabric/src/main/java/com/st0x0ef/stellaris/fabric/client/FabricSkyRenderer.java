package com.st0x0ef.stellaris.fabric.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FabricSkyRenderer {

//    public static void renderSky(WorldRenderContext context) {
//        ResourceKey<Level> location = context.world().dimension();
//
//        if (SkyPropertiesData.SKY_PROPERTIES.containsKey(location)) {
//            SkyRenderer skyRenderer = SkyPropertiesData.SKY_PROPERTIES.get(location);
//            int ticks = ((LevelRendererAccessor) Minecraft.getInstance().levelRenderer).stellaris$ticks();
//
//            PoseStack poseStack = context.matrixStack();
//            if (poseStack != null) {
//                skyRenderer.render(
//                        context.world(),
//                        ticks,
//                        context.tickDelta(),
//                        poseStack,
//                        context.camera(),
//                        context.projectionMatrix(),
//                        false,
//                        () -> {}
//                );
//            } else {
//                Stellaris.LOG.error("PoseStack is null");
//            }
//        }
//    }
}
