package com.st0x0ef.stellaris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
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
import com.st0x0ef.stellaris.client.screens.RocketMenuScreen;
import com.st0x0ef.stellaris.client.screens.RocketStationScreen;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.events.Events;
import com.st0x0ef.stellaris.common.registry.*;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stellaris {
    public static final String MODID = "stellaris";
    public static final Logger LOG = LoggerFactory.getLogger("Stellaris");
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();
    public static void init() {
        CustomConfig.init();

        SoundRegistry.SOUNDS.register();
        FluidRegistry.FLUIDS.register();
        EffectsRegistry.MOB_EFFECT.register();
        ParticleRegistry.PARTICLES.register();
        BlocksRegistry.BLOCKS.register();
        MenuTypesRegistry.MENU_TYPE.register();
        EntityRegistry.BLOCK_ENTITY_TYPE.register();
        EntityRegistry.ENTITY_TYPE.register();
        EntityRegistry.SENSOR.register();
        EntityRegistry.registerAttributes();
        FeaturesRegistry.FEATURES.register();
        ItemsRegistry.ITEMS.register();
        CreativeTabsRegistry.TABS.register();

        RecipesRegistry.register();

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new StellarisData());
        Events.registerEvents();
    }
}
