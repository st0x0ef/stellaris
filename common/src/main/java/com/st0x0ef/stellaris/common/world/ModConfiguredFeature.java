package com.st0x0ef.stellaris.common.world;

import com.google.common.base.Suppliers;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

import java.util.List;
import java.util.function.Supplier;

public class ModConfiguredFeature {
    // OVERWORLD
    static final ResourceKey<ConfiguredFeature<?, ?>> STEEL_ORE_KEY = registerKey("steel_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> STEEL_ORE_DEEPSLATE_ORE_KEY = registerKey("steel_ore_deepslate_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LAKE_OIL = registerKey("lake_oil");

    // MARS
    public static final ResourceKey<ConfiguredFeature<?, ?>> MARS_DIAMOND_ORE_KEY = registerKey("mars_diamond_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MARS_ICE_SHARD_ORE_KEY = registerKey("mars_ice_shard_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MARS_IRON_ORE_KEY = registerKey("mars_iron_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MARS_OSTRUM_ORE_KEY = registerKey("mars_ostrum_ore_key");

    // MERCURY
    public static final ResourceKey<ConfiguredFeature<?, ?>> MERCURY_IRON_ORE_KEY = registerKey("mercury_iron_ore_key");

    // MOON
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_CHEESE_ORE_KEY = registerKey("moon_cheese_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_DESH_ORE_KEY = registerKey("moon_desh_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_ICE_SHARD_ORE_KEY = registerKey("moon_ice_shard_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_IRON_ORE_KEY = registerKey("moon_iron_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_SOUL_SOIL_KEY = registerKey("moon_soul_soil_key");

    // VENUS
    public static final ResourceKey<ConfiguredFeature<?, ?>> VENUS_CALORITE_ORE_KEY = registerKey("venus_calorite_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VENUS_COAL_ORE_KEY = registerKey("venus_coal_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VENUS_DIAMOND_ORE_KEY = registerKey("venus_diamond_ore_key");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VENUS_GOLD_ORE_KEY = registerKey("venus_gold_ore_key");



    // MARS
    public static final Supplier<List<OreConfiguration.TargetBlockState>> MARS_DIAMOND_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.MARS_STONE.get()), BlocksRegistry.MARS_DIAMOND_ORE.get().defaultBlockState())));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> MARS_ICE_SHARD_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.MARS_STONE.get()), BlocksRegistry.MARS_ICE_SHARD_ORE.get().defaultBlockState())));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> STEEL_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(Blocks.STONE), BlocksRegistry.STEEL_ORE.get().defaultBlockState())));
    public static final Supplier<List<OreConfiguration.TargetBlockState>> STEEL_ORE_DEEPSLATE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(Blocks.DEEPSLATE), BlocksRegistry.DEEPSLATE_STEEL_ORE.get().defaultBlockState())));



    public static final Supplier<List<OreConfiguration.TargetBlockState>> MARS_IRON_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.MARS_STONE.get()), BlocksRegistry.MARS_IRON_ORE.get().defaultBlockState())));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> MARS_OSTRUM_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.MARS_STONE.get()), BlocksRegistry.MARS_OSTRUM_ORE.get().defaultBlockState())));

    // MERCURY
    public static final Supplier<List<OreConfiguration.TargetBlockState>> MERCURY_IRON_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.MERCURY_STONE.get()), BlocksRegistry.MERCURY_IRON_ORE.get().defaultBlockState())));

    // MOON
    public static final Supplier<List<OreConfiguration.TargetBlockState>> MOON_DESH_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.MOON_STONE.get()), BlocksRegistry.MOON_DESH_ORE.get().defaultBlockState())));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> MOON_ICE_SHARD_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.MOON_STONE.get()), BlocksRegistry.MOON_ICE_SHARD_ORE.get().defaultBlockState())));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> MOON_IRON_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.MOON_STONE.get()), BlocksRegistry.MOON_IRON_ORE.get().defaultBlockState())));
    public static final Supplier<List<OreConfiguration.TargetBlockState>> MOON_SOUL_SOIL_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.MOON_STONE.get()), Blocks.SOUL_SOIL.defaultBlockState())));

    // VENUS
    public static final Supplier<List<OreConfiguration.TargetBlockState>> VENUS_COAL_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.VENUS_STONE.get()), BlocksRegistry.VENUS_COAL_ORE.get().defaultBlockState())));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> VENUS_DIAMOND_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.VENUS_STONE.get()), BlocksRegistry.VENUS_DIAMOND_ORE.get().defaultBlockState())));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> VENUS_GOLD_ORE_REPLACEABLES = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(BlocksRegistry.VENUS_STONE.get()), BlocksRegistry.VENUS_GOLD_ORE.get().defaultBlockState())));

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Stellaris.MODID, name));
    }
}
