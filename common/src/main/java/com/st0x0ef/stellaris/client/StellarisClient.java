package com.st0x0ef.stellaris.client;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.overlays.JetSuitOverlay;
import com.st0x0ef.stellaris.client.overlays.LanderOverlay;
import com.st0x0ef.stellaris.client.overlays.RocketBarOverlay;
import com.st0x0ef.stellaris.client.overlays.RocketStartOverlay;
import com.st0x0ef.stellaris.client.particles.*;
import com.st0x0ef.stellaris.client.registries.KeyMappingsRegistry;
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
import com.st0x0ef.stellaris.common.data.renderer.SkyPack;
import com.st0x0ef.stellaris.common.data.screen.MoonPack;
import com.st0x0ef.stellaris.common.data.screen.PlanetPack;
import com.st0x0ef.stellaris.common.data.screen.StarPack;
import com.st0x0ef.stellaris.common.handlers.GlobalExceptionHandler;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import com.st0x0ef.stellaris.common.registry.ParticleRegistry;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.server.packs.PackType;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;


public class StellarisClient {
    @Environment(EnvType.CLIENT)
    public static void initClient() {
        registerPacks();

        Minecraft.getInstance().execute(() -> {
            setupOpenGLDebugMessageCallback();
            Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        });

        registerParticle();

        if(Platform.isFabric()) {
            registerEntityRenderer();
            registerEntityModelLayer();
        }

        KeyMappingsRegistry.register();

        registerScreen();
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
        EntityModelLayerRegistry.register(LanderModel.LAYER_LOCATION, LanderModel::createBodyLayer);

        EntityModelLayerRegistry.register(TinyRocketModel.LAYER_LOCATION, TinyRocketModel::createBodyLayer);
        EntityModelLayerRegistry.register(SmallRocketModel.LAYER_LOCATION, SmallRocketModel::createBodyLayer);
        EntityModelLayerRegistry.register(NormalRocketModel.LAYER_LOCATION, NormalRocketModel::createBodyLayer);
        EntityModelLayerRegistry.register(BigRocketModel.LAYER_LOCATION, BigRocketModel::createBodyLayer);
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

        EntityRendererRegistry.register(EntityRegistry.NORMAL_ROCKET, NormalRocketRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TINY_ROCKET, TinyRocketRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SMALL_ROCKET, SmallRocketRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BIG_ROCKET, BigRocketRenderer::new);

        EntityRendererRegistry.register(EntityRegistry.LANDER, LanderRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntityRegistry.GLOBE_BLOCK_ENTITY.get(), GlobeBlockRenderer::new);
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
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.MILKYWAY_MENU.get(), MilkyWayScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.LANDER_MENU.get(), LanderScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.OXYGEN_DISTRIBUTOR.get(), OxygenDistributorScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.WATER_SEPARATOR_MENU.get(), WaterSeparatorScreen::new);
        MenuRegistry.registerScreenFactory(MenuTypesRegistry.FUEL_REFINERY.get(), FuelRefineryScreen::new);
    }

    public static void registerOverlays() {
        ClientGuiEvent.RENDER_HUD.register(RocketStartOverlay::render);
        ClientGuiEvent.RENDER_HUD.register(RocketBarOverlay::render);
        ClientGuiEvent.RENDER_HUD.register(LanderOverlay::render);
        ClientGuiEvent.RENDER_HUD.register(JetSuitOverlay::render);

    }

    public static void setupOpenGLDebugMessageCallback() {
        if (GL.getCapabilities().GL_KHR_debug) {
            GL43.glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
                if (id == 1281) {
                    return;
                }
                String errorMessage = GLDebugMessageCallback.getMessage(length, message);
                Stellaris.LOG.error("OpenGL debug message: id={}, source={}, type={}, severity={}, message='{}'",
                        id, source, type, severity, errorMessage);
            }, 0);
            GL43.glEnable(GL43.GL_DEBUG_OUTPUT);
            GL43.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);
        }
    }

    public static void registerPacks() {
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new StarPack(Stellaris.GSON));
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new PlanetPack(Stellaris.GSON));
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new MoonPack(Stellaris.GSON));
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new SkyPack(Stellaris.GSON));
    }
}
