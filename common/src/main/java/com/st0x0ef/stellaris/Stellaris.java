package com.st0x0ef.stellaris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.st0x0ef.stellaris.client.StellarisClient;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.data.renderer.SkyPack;
import com.st0x0ef.stellaris.common.data.screen.MoonPack;
import com.st0x0ef.stellaris.common.data.screen.PlanetPack;
import com.st0x0ef.stellaris.common.data.screen.StarPack;
import com.st0x0ef.stellaris.common.events.Events;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.network.packets.SyncPlanetsDatapack;
import com.st0x0ef.stellaris.common.registry.*;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class Stellaris {
    public static final String MODID = "stellaris";
    public static final Logger LOG = LoggerFactory.getLogger("Stellaris");
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();

    public static void init() {
        CustomConfig.init();
        NetworkRegistry.registerC2S();
        NetworkRegistry.registerS2C();
        EntityData.register();
        if(!Platform.getEnvironment().equals(Env.SERVER)) {
            StellarisClient.registerPacks();
        }

        SoundRegistry.SOUNDS.register();
        EffectsRegistry.MOB_EFFECTS.register();
        DataComponentsRegistry.DATA_COMPONENT_TYPE.register();
        FluidRegistry.FLUIDS.register();
        ParticleRegistry.PARTICLES.register();
        BlocksRegistry.BLOCKS.register();
        EntityRegistry.ENTITY_TYPE.register();
        EntityRegistry.SENSOR.register();
        BlockEntityRegistry.BLOCK_ENTITY_TYPE.register();
        ItemsRegistry.ITEMS.register();
        ArmorMaterialsRegistry.ARMOR_MATERIAL.register();
        CreativeTabsRegistry.TABS.register();
        MenuTypesRegistry.MENU_TYPE.register();
        FeaturesRegistry.FEATURES.register();
        CommandsRegistry.register();
        BiomeModificationsRegistry.register();
        Events.registerEvents();
        LookupApiRegistry.register();
        RecipesRegistry.register();
    }

    public static void onDatapackSyncEvent(ServerPlayer player) {
        StellarisData.PLANETS.forEach(((resourceKey, planet) -> {
            RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.getServer().registryAccess());
            NetworkRegistry.sendToPlayer(player, NetworkRegistry.SYNC_PLANET_DATAPACK_ID, SyncPlanetsDatapack.encode(new SyncPlanetsDatapack(resourceKey, planet), buffer));
        }));
    }

    public static void onAddReloadListenerEvent(BiConsumer<ResourceLocation, PreparableReloadListener> registry) {
        registry.accept(new ResourceLocation(Stellaris.MODID, "planets"), new StellarisData());

        registry.accept(new ResourceLocation(Stellaris.MODID, "stars_pack"), new StarPack());
        registry.accept(new ResourceLocation(Stellaris.MODID, "planets_pack"), new PlanetPack());
        registry.accept(new ResourceLocation(Stellaris.MODID, "moon_packs"), new MoonPack());
        registry.accept(new ResourceLocation(Stellaris.MODID, "sky_packs"), new SkyPack());

    }
}
