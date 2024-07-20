package com.st0x0ef.stellaris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.client.skys.record.SkyPropertiesData;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.data.screen.MoonPack;
import com.st0x0ef.stellaris.common.data.screen.PlanetPack;
import com.st0x0ef.stellaris.common.data.screen.StarPack;
import com.st0x0ef.stellaris.common.events.Events;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.network.packets.SyncPlanetsDatapackPacket;
import com.st0x0ef.stellaris.common.registry.*;
import com.st0x0ef.stellaris.common.systems.core.CommonStorageLib;
import com.st0x0ef.stellaris.common.systems.core.fluid.util.FluidStorageData;
import com.st0x0ef.stellaris.common.systems.core.item.util.ItemStorageData;
import com.st0x0ef.stellaris.common.systems.data.DataManager;
import com.st0x0ef.stellaris.common.systems.data.DataManagerRegistry;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
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

    public static final DataManagerRegistry DATA_MANAGER_REGISTRY = new DataManagerRegistry(MODID);
    public static final DataManager<FluidStorageData> FLUID_CONTENTS = DATA_MANAGER_REGISTRY.builder(FluidStorageData.DEFAULT).serialize(FluidStorageData.CODEC).networkSerializer(FluidStorageData.NETWORK_CODEC).withDataComponent().copyOnDeath().buildAndRegister("fluids");
    public static final DataManager<ItemStorageData> ITEM_CONTENTS = DATA_MANAGER_REGISTRY.builder(ItemStorageData.DEFAULT).serialize(ItemStorageData.CODEC).networkSerializer(ItemStorageData.NETWORK_CODEC).withDataComponent().copyOnDeath().buildAndRegister("items");
    public static final DataManager<Long> VALUE_CONTENT = DATA_MANAGER_REGISTRY.builder(() -> 0L).serialize(Codec.LONG).networkSerializer(ByteBufCodecs.VAR_LONG).withDataComponent().copyOnDeath().buildAndRegister("energy");

    public static void init() {
        CustomConfig.init();

        ProcessorsRegistry.STRUCTURE_PROCESSORS.register();
        EntityData.register();
        NetworkRegistry.init();
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
        RecipesRegistry.register();
        DATA_MANAGER_REGISTRY.init();
        CommonStorageLib.init();

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new StellarisData());
    }

    public static void onDatapackSyncEvent(ServerPlayer player, boolean joined) {
        if (joined) {
            NetworkManager.sendToPlayer(player, new SyncPlanetsDatapackPacket(StellarisData.getPlanets()));
        }
    }

    public static void onAddReloadListenerEvent(BiConsumer<ResourceLocation, PreparableReloadListener> registry) {
        registry.accept(new ResourceLocation(Stellaris.MODID, "planets"), new StellarisData());

        registry.accept(new ResourceLocation(Stellaris.MODID, "sky_renderer"), new SkyPropertiesData());
        registry.accept(new ResourceLocation(Stellaris.MODID, "stars_pack"), new StarPack());
        registry.accept(new ResourceLocation(Stellaris.MODID, "planets_pack"), new PlanetPack());
        registry.accept(new ResourceLocation(Stellaris.MODID, "moon_packs"), new MoonPack());
    }
}
