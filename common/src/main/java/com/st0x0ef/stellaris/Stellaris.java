package com.st0x0ef.stellaris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.events.Events;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.registry.*;
import dev.architectury.registry.ReloadListenerRegistry;
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

        NetworkRegistry.register();
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
        CommandsRegistry.register();
        RecipesRegistry.register();

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new StellarisData());
        Events.registerEvents();
    }
}
