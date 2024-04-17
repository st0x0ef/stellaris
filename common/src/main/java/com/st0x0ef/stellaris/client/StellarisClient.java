package com.st0x0ef.stellaris.client;

import com.st0x0ef.stellaris.client.particles.*;
import com.st0x0ef.stellaris.client.renderers.entities.alien.AlienModel;
import com.st0x0ef.stellaris.client.renderers.entities.alien.AlienRenderer;
import com.st0x0ef.stellaris.client.renderers.entities.alienzombie.AlienZombieModel;
import com.st0x0ef.stellaris.client.renderers.entities.alienzombie.AlienZombieRenderer;
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
import com.st0x0ef.stellaris.client.renderers.globe.GlobeModel;
import com.st0x0ef.stellaris.client.renderers.globe.GlobeBlockRenderer;
import com.st0x0ef.stellaris.client.screens.RocketMenuScreen;
import com.st0x0ef.stellaris.client.screens.RocketStationScreen;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import com.st0x0ef.stellaris.common.registry.ParticleRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class StellarisClient {
    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.register(AlienModel.LAYER_LOCATION, AlienModel::createBodyLayer);
        EntityModelLayerRegistry.register(AlienZombieModel.LAYER_LOCATION, AlienZombieModel::createBodyLayer);
        EntityModelLayerRegistry.register(MartianRaptorModel.LAYER_LOCATION, MartianRaptorModel::createBodyLayer);
        EntityModelLayerRegistry.register(PygroModel.LAYER_LOCATION, PygroModel::createBodyLayer);
        EntityModelLayerRegistry.register(MoglerModel.LAYER_LOCATION, MoglerModel::createBodyLayer);
        EntityModelLayerRegistry.register(StarCrawlerModel.LAYER_LOCATION, StarCrawlerModel::createBodyLayer);

        EntityModelLayerRegistry.register(GlobeModel.LAYER_LOCATION, GlobeModel::createLayer);
    }
    public static void registerEntityRenderer() {
        EntityRendererRegistry.register(EntityRegistry.ALIEN, AlienRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ALIEN_ZOMBIE, AlienZombieRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MARTIAN_RAPTOR, MartianRaptorRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PYGRO, PygroRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PYGRO_BRUTE, PygroBruteRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MOGLER, MoglerRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.STAR_CRAWLER, StarCrawlerRenderer::new);

        EntityRendererRegistry.register(EntityRegistry.ICE_SPIT, renderManager -> new ThrownItemRenderer<>(renderManager, 1, true));
        EntityRendererRegistry.register(EntityRegistry.ICE_SHARD_ARROW, IceShardArrowRenderer::new);

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
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.ROCKET_MENU.get(), RocketMenuScreen::new);
    }
}
