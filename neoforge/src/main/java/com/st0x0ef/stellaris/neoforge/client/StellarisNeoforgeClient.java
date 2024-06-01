package com.st0x0ef.stellaris.neoforge.client;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.client.renderers.entities.alien.AlienModel;
import com.st0x0ef.stellaris.client.renderers.entities.alien.AlienRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.alienzombie.AlienZombieModel;
import com.st0x0ef.stellaris.client.renderers.entities.alienzombie.AlienZombieRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.cheeseboss.CheeseBossModel;
import com.st0x0ef.stellaris.client.renderers.entities.cheeseboss.CheeseBossRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.martianraptor.MartianRaptorModel;
import com.st0x0ef.stellaris.client.renderers.entities.martianraptor.MartianRaptorRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.mogler.MoglerModel;
import com.st0x0ef.stellaris.client.renderers.entities.mogler.MoglerRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.projectiles.IceShardArrowRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.pygro.PygroBruteRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.pygro.PygroModel;
import com.st0x0ef.stellaris.client.renderers.entities.pygro.PygroRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.starcrawler.StarCrawlerModel;
import com.st0x0ef.stellaris.client.renderers.entities.starcrawler.StarCrawlerRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.lander.LanderModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.lander.LanderRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.big.BigRocketModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.big.BigRocketRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.normal.NormalRocketModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.normal.NormalRocketRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.small.SmallRocketModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.small.SmallRocketRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.tiny.TinyRocketModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.tiny.TinyRocketRenderer;
import com.st0x0ef.stellaris.client.renderers.globe.GlobeBlockRenderer;
import com.st0x0ef.stellaris.client.renderers.globe.GlobeModel;
import com.st0x0ef.stellaris.client.screens.*;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Stellaris.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class StellarisNeoforgeClient {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(StellarisClient::initClient);
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
}
