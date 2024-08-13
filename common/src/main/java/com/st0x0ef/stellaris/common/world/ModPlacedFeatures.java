package com.st0x0ef.stellaris.common.world;

import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModPlacedFeatures {

    // OVERWORLD
    public static final ResourceKey<PlacedFeature> STEEL_ORE_PLACED_KEY = createKey("steel_ore");
    public static final ResourceKey<PlacedFeature> STEEL_ORE_DEEPSLATE_PLACED_KEY = createKey("steel_ore_deepslate");

    public static final ResourceKey<PlacedFeature> LAKE_OIL_UNDERGROUND = createKey("lake_oil_underground");
    public static final ResourceKey<PlacedFeature> LAKE_OIL_SURFACE = createKey("lake_oil_surface");


    // MARS
    public static final ResourceKey<PlacedFeature> MARS_DIAMOND_PLACED_KEY = createKey("mars_diamond_ore");
    public static final ResourceKey<PlacedFeature> MARS_ICE_SHARD_PLACED_KEY = createKey("mars_ice_shard_ore");
    public static final ResourceKey<PlacedFeature> MARS_IRON_PLACED_KEY = createKey("mars_iron_ore");
    public static final ResourceKey<PlacedFeature> MARS_OSTRUM_PLACED_KEY = createKey("mars_ostrum_ore");

    // MERCURY
    public static final ResourceKey<PlacedFeature> MERCURY_IRON_PLACED_KEY = createKey("mercury_iron_ore");

    // MOON
    public static final ResourceKey<PlacedFeature> MOON_DESH_PLACED_KEY = createKey("moon_desh_ore");
    public static final ResourceKey<PlacedFeature> MOON_ICE_SHARD_PLACED_KEY = createKey("moon_ice_shard_ore");
    public static final ResourceKey<PlacedFeature> MOON_IRON_PLACED_KEY = createKey("moon_iron_ore");
    public static final ResourceKey<PlacedFeature> MOON_SOUL_SOIL_PLACED_KEY = createKey("moon_soul_soil");

    // VENUS
    public static final ResourceKey<PlacedFeature> VENUS_COAL_PLACED_KEY = createKey("venus_coal_ore");
    public static final ResourceKey<PlacedFeature> VENUS_DIAMOND_PLACED_KEY = createKey("venus_diamond_ore");
    public static final ResourceKey<PlacedFeature> VENUS_GOLD_PLACED_KEY = createKey("venus_gold_ore");

    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Stellaris.MODID, name));
    }
}
