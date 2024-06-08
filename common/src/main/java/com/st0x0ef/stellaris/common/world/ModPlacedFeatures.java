package com.st0x0ef.stellaris.common.world;

import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {

    // MARS
    public static final ResourceKey<PlacedFeature> MARS_DIAMOND_PLACED_KEY = createKey("mars_diamond_ore");

    public static final ResourceKey<PlacedFeature> MARS_ICE_SHARD_PLACED_KEY = createKey("mars_ice_shard_ore");

    public static final ResourceKey<PlacedFeature> MARS_IRON_PLACED_KEY = createKey("mars_iron_ore");

    public static final ResourceKey<PlacedFeature> MARS_OSTRUM_PLACED_KEY = createKey("mars_ostrum_ore");

    // MERCURY
    public static final ResourceKey<PlacedFeature> MERCURY_IRON_PLACED_KEY = createKey("mercury_iron_ore");

    // MOON
    public static final ResourceKey<PlacedFeature> MOON_CHEESE_PLACED_KEY = createKey("moon_cheese_ore");
    public static final ResourceKey<PlacedFeature> MOON_DESH_PLACED_KEY = createKey("moon_desh_ore");

    public static final ResourceKey<PlacedFeature> MOON_ICE_SHARD_PLACED_KEY = createKey("moon_ice_shard_ore");

    public static final ResourceKey<PlacedFeature> MOON_IRON_PLACED_KEY = createKey("moon_iron_ore");
    public static final ResourceKey<PlacedFeature> MOON_SOUL_SOIL_PLACED_KEY = createKey("moon_soul_soil");

    // VENUS
    public static final ResourceKey<PlacedFeature> VENUS_CALORITE_PLACED_KEY = createKey("venus_calorite_ore");
    public static final ResourceKey<PlacedFeature> VENUS_COAL_PLACED_KEY = createKey("venus_coal_ore");

    public static final ResourceKey<PlacedFeature> VENUS_DIAMOND_PLACED_KEY = createKey("venus_diamond_ore");

    public static final ResourceKey<PlacedFeature> VENUS_GOLD_PLACED_KEY = createKey("venus_gold_ore");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);


        // MARS
        register(context, MARS_DIAMOND_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.MARS_DIAMOND_ORE_KEY),
                OrePlacement.commonOrePlacement(7,
                        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
        register(context, MARS_ICE_SHARD_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.MARS_ICE_SHARD_ORE_KEY),
                OrePlacement.commonOrePlacement(8,
                        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-32), VerticalAnchor.aboveBottom(32))));
        register(context, MARS_IRON_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.MARS_IRON_ORE_KEY),
                OrePlacement.commonOrePlacement(8,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));
        register(context, MARS_OSTRUM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.MARS_OSTRUM_ORE_KEY),
                OrePlacement.commonOrePlacement(6,
                        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));

        // MERCURY
        register(context, MERCURY_IRON_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.MERCURY_IRON_ORE_KEY),
                OrePlacement.commonOrePlacement(20,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-80), VerticalAnchor.absolute(192))));

        // MOON
        register(context, MOON_CHEESE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.MOON_CHEESE_ORE_KEY),
                OrePlacement.commonOrePlacement(20,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-80), VerticalAnchor.absolute(192))));
        register(context, MOON_DESH_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.MOON_DESH_ORE_KEY),
                OrePlacement.commonOrePlacement(7,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-80), VerticalAnchor.absolute(80))));
        register(context, MOON_ICE_SHARD_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.MOON_ICE_SHARD_ORE_KEY),
                OrePlacement.commonOrePlacement(8,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(32))));
        register(context, MOON_IRON_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.MOON_IRON_ORE_KEY),
                OrePlacement.commonOrePlacement(10,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));
        register(context, MOON_SOUL_SOIL_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.MOON_SOUL_SOIL_KEY),
                OrePlacement.commonOrePlacement(20, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(100))));

        // VENUS
        register(context, VENUS_CALORITE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.VENUS_CALORITE_ORE_KEY),
                OrePlacement.commonOrePlacement(6,
                        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
        register(context, VENUS_COAL_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.VENUS_COAL_ORE_KEY),
                OrePlacement.commonOrePlacement(20,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-80), VerticalAnchor.absolute(192))));
        register(context, VENUS_DIAMOND_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.VENUS_DIAMOND_ORE_KEY),
                OrePlacement.commonOrePlacement(7,
                        HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
        register(context, VENUS_GOLD_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeature.VENUS_GOLD_ORE_KEY),
                OrePlacement.commonOrePlacement(4,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
    }
    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Stellaris.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }

}
