package com.st0x0ef.stellaris.neoforge.client;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.platform.PlatformClientUtilsHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = Stellaris.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class StellarisNeoforgeClient {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(StellarisClient::initClient);
        NeoForge.EVENT_BUS.addListener(StellarisNeoforgeClient::renderSky);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        StellarisClient.registerEntityRenderer();
    }

    @SubscribeEvent
    public static void registerEntityLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        StellarisClient.registerEntityModelLayer();
    }

    @SubscribeEvent
    public static void registerScreen(RegisterMenuScreensEvent event) {
        StellarisClient.registerScreen();
    }

    public static void renderSky(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SKY)) {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.player.clientLevel;
            if(level == null) return;

            if (PlatformClientUtilsHelper.DIMENSION_RENDERERS.containsKey(level.dimension())) {
                Stellaris.LOG.error("renderingh ");
                SkyRenderer renderer = PlatformClientUtilsHelper.DIMENSION_RENDERERS.get(level.dimension());
                renderer.render(level, event.getRenderTick(), event.getPartialTick(), event.getPoseStack(), event.getCamera(), event.getProjectionMatrix(), false, () -> {});
            }
        }
    }
}
