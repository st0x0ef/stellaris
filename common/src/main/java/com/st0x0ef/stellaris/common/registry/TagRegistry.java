package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class TagRegistry {

    /** ENTITIES */
    public static final TagKey<EntityType<?>> ENTITY_NO_OXYGEN_NEEDED_TAG = TagKey.create(Registries.ENTITY_TYPE, ResourceLocationUtils.id("no_oxygen_needed"));
    public static final TagKey<EntityType<?>> ENTITY_PLANET_FIRE_TAG = TagKey.create(Registries.ENTITY_TYPE, ResourceLocationUtils.id("planet_fire"));
    public static final TagKey<EntityType<?>> ENTITY_VENUS_RAIN_TAG = TagKey.create(Registries.ENTITY_TYPE, ResourceLocationUtils.id("venus_rain"));
    public static final TagKey<EntityType<?>> ENTITY_BOSS = TagKey.create(Registries.ENTITY_TYPE, ResourceLocationUtils.id("boss"));

    public static final TagKey<EntityType<?>> ENTITY_RADIATION_INVULNERABLE_TAG = TagKey.create(Registries.ENTITY_TYPE, ResourceLocationUtils.id("radiations_invulnerable"));

    /** FLUIDS */
    public static final TagKey<Fluid> FLUID_VEHICLE_FUEL_TAG = TagKey.create(Registries.FLUID, ResourceLocationUtils.id("vehicle_fuel"));
    public static final TagKey<Fluid> FLUID_OIL_FLUID_TAG = TagKey.create(Registries.FLUID, ResourceLocationUtils.id("oil"));

    /** BIOMES */
    public static final TagKey<Biome> MARS_BIOMES_TAG = TagKey.create(Registries.BIOME, ResourceLocationUtils.id("mars_biomes"));
    public static final TagKey<Biome> MERCURY_BIOMES_TAG = TagKey.create(Registries.BIOME, ResourceLocationUtils.id("mercury_biomes"));
    public static final TagKey<Biome> MOON_BIOMES_TAG = TagKey.create(Registries.BIOME, ResourceLocationUtils.id("moon_biomes"));
    public static final TagKey<Biome> VENUS_BIOMES_TAG = TagKey.create(Registries.BIOME, ResourceLocationUtils.id("venus_biomes"));
    public static final TagKey<Biome> SANDSTORM_BIOMES_TAG = TagKey.create(Registries.BIOME, ResourceLocationUtils.id("sandstorm_biomes"));

    /** ROCKET UPGRADE */
    public static final TagKey<Item> ROCKET_UPGRADE_TAG = TagKey.create(Registries.ITEM, ResourceLocationUtils.id("rocket_upgrade"));

    /** ITEMS */
    public static final TagKey<Item> SPACE_FOOD = TagKey.create(Registries.ITEM, ResourceLocationUtils.id("space_food"));
    public static final TagKey<Item> COAL_GENERATOR_FUEL_TAG = TagKey.create(Registries.ITEM, ResourceLocationUtils.id("coal_generator_fuel"));
    public static final TagKey<Item> RADIOACTIVE_GENERATOR_FUEL_TAG = TagKey.create(Registries.ITEM, ResourceLocationUtils.id("radioactive_generator_fuel"));
    public static final TagKey<Item> ROCKET_SKIN = TagKey.create(Registries.ITEM, ResourceLocationUtils.id("rocket_skin"));

    /** BLOCKS */
    public static final TagKey<Block> SPACE_STATION_CAN_SPAWN_ON = TagKey.create(Registries.BLOCK, ResourceLocationUtils.id("space_station_can_spawn_on"));

    public static final TagKey<Block> ENERGY_BLOCK_TAG = TagKey.create(Registries.BLOCK, ResourceLocationUtils.id("energy_block"));
    public static final TagKey<MobEffect> RADIOACTIVEEFFECT = TagKey.create(Registries.MOB_EFFECT, ResourceLocationUtils.id("radioactive"));
    public static final TagKey<Block> INCORRECT_FOR_STEEL_TOOL = TagKey.create(Registries.BLOCK, ResourceLocationUtils.id("incorrect_for_steel_tools"));
}
