package com.st0x0ef.stellaris.client;

import com.st0x0ef.stellaris.client.events.ClientEvents;
import com.st0x0ef.stellaris.client.overlays.LanderOverlay;
import com.st0x0ef.stellaris.client.overlays.RocketBarOverlay;
import com.st0x0ef.stellaris.client.overlays.RocketStartOverlay;
import com.st0x0ef.stellaris.client.particles.*;
import com.st0x0ef.stellaris.client.registries.KeyMappings;
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
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.normal.NormalRocketModel;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.normal.NormalRocketRenderer;
import com.st0x0ef.stellaris.client.renderers.globe.GlobeBlockRenderer;
import com.st0x0ef.stellaris.client.renderers.globe.GlobeModel;
import com.st0x0ef.stellaris.client.screens.*;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import com.st0x0ef.stellaris.common.registry.ParticleRegistry;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class StellarisClient {

    public static void initClient() {
        registerParticle();
        registerEntityModelLayer();
        registerEntityRenderer();
        registerScreen();
        registerKey();
        ClientEvents.registerEvents();
        registerOverlays();
    }
    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.register(AlienModel.LAYER_LOCATION, AlienModel::createBodyLayer);
        EntityModelLayerRegistry.register(AlienZombieModel.LAYER_LOCATION, AlienZombieModel::createBodyLayer);
        EntityModelLayerRegistry.register(MartianRaptorModel.LAYER_LOCATION, MartianRaptorModel::createBodyLayer);
        EntityModelLayerRegistry.register(PygroModel.LAYER_LOCATION, PygroModel::createBodyLayer);
        EntityModelLayerRegistry.register(MoglerModel.LAYER_LOCATION, MoglerModel::createBodyLayer);
        EntityModelLayerRegistry.register(StarCrawlerModel.LAYER_LOCATION, StarCrawlerModel::createBodyLayer);
        EntityModelLayerRegistry.register(CheeseBossModel.LAYER_LOCATION, CheeseBossModel::createBodyLayer);

        EntityModelLayerRegistry.register(GlobeModel.LAYER_LOCATION, GlobeModel::createLayer);
        EntityModelLayerRegistry.register(NormalRocketModel.LAYER_LOCATION, NormalRocketModel::createBodyLayer);
        EntityModelLayerRegistry.register(LanderModel.LAYER_LOCATION, LanderModel::createBodyLayer);

    }
    public static void registerEntityRenderer() {
        EntityRendererRegistry.register(EntityRegistry.ALIEN, AlienRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ALIEN_ZOMBIE, AlienZombieRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MARTIAN_RAPTOR, MartianRaptorRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PYGRO, PygroRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PYGRO_BRUTE, PygroBruteRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MOGLER, MoglerRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.STAR_CRAWLER, StarCrawlerRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CHEESE_BOSS, CheeseBossRenderer::new);

        EntityRendererRegistry.register(EntityRegistry.ICE_SPIT, renderManager -> new ThrownItemRenderer<>(renderManager, 1, true));
        EntityRendererRegistry.register(EntityRegistry.ICE_SHARD_ARROW, IceShardArrowRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ROCKET, NormalRocketRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.LANDER, LanderRenderer::new);


        BlockEntityRendererRegistry.register(EntityRegistry.GLOBE_BLOCK_ENTITY.get() ,GlobeBlockRenderer::new);
    }

    public static void registerParticle() {
        ParticleProviderRegistry.register(ParticleRegistry.VENUS_RAIN_PARTICLE.get(), VenusRainParticle.ParticleFactory::new);
        ParticleProviderRegistry.register(ParticleRegistry.LARGE_FLAME_PARTICLE.get(), LargeFlameParticle.ParticleFactory::new);
        ParticleProviderRegistry.register(ParticleRegistry.LARGE_SMOKE_PARTICLE.get(), LargeSmokeParticle.ParticleFactory::new);
        ParticleProviderRegistry.register(ParticleRegistry.SMALL_FLAME_PARTICLE.get(), SmallFlameParticle.ParticleFactory::new);
        ParticleProviderRegistry.register(ParticleRegistry.SMALL_SMOKE_PARTICLE.get(), SmallSmokeParticle.ParticleFactory::new);
    }

    public static void registerScreen() {
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.ROCKET_STATION.get(), RocketStationScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.ROCKET_MENU.get(), RocketScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.VACUMATOR_MENU.get(), VacumatorScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.SOLAR_PANEL_MENU.get(), SolarPanelScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.COAL_GENERATOR_MENU.get(), CoalGeneratorScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.RADIOACTIVE_GENERATOR_MENU.get(), RadioactiveGeneratorScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.PLANET_SELECTION_MENU.get(), PlanetSelectionScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.LANDER_MENU.get(), LanderScreen::new);
    }

    public static void registerKey() {
        KeyMappingRegistry.register(KeyMappings.ROCKET_START);
    }

    public static void registerOverlays() {
        ClientGuiEvent.RENDER_HUD.register(RocketStartOverlay::render);
        ClientGuiEvent.RENDER_HUD.register(RocketBarOverlay::render);
        ClientGuiEvent.RENDER_HUD.register(LanderOverlay::render);

    }
}
