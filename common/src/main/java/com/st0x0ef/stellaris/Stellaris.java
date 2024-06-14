package com.st0x0ef.stellaris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.events.Events;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.network.packets.SyncPlanetsDatapack;
import com.st0x0ef.stellaris.common.registry.*;
import dev.architectury.registry.ReloadListenerRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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
        EntityData.register();

        DataComponentsRegistry.DATA_COMPONENT_TYPE.register();
        RecipesRegistry.register();

        SoundRegistry.SOUNDS.register();
        FluidRegistry.FLUIDS.register();
        EffectsRegistry.MOB_EFFECT.register();
        ParticleRegistry.PARTICLES.register();
        BlocksRegistry.BLOCKS.register();
        EntityRegistry.ENTITY_TYPE.register();
        EntityRegistry.SENSOR.register();
        BlockEntityRegistry.BLOCK_ENTITY_TYPE.register();
        ItemsRegistry.ITEMS.register();
        CreativeTabsRegistry.TABS.register();
        MenuTypesRegistry.MENU_TYPE.register();
        FeaturesRegistry.FEATURES.register();
        CommandsRegistry.register();
        //LookupApiRegistry.register();
        BiomeModificationsRegistry.register();
        ReloadListenerRegistry.register(PackType.SERVER_DATA, new StellarisData());
        Events.registerEvents();
        LookupApiRegistry.register();
    }

    public static void onDatapackSyncEvent(ServerPlayer player) {
        StellarisData.PLANETS.forEach(((resourceKey, planet) -> {
            RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
            NetworkRegistry.sendToPlayer(player, NetworkRegistry.SYNC_PLANET_DATAPACK_ID, SyncPlanetsDatapack.encode(new SyncPlanetsDatapack(resourceKey, planet), buffer));
        }));
    }

}
