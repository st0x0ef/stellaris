package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class TagRegistry {

    /** ENTITIES */
    public static final TagKey<EntityType<?>> ENTITY_NO_OXYGEN_NEEDED_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Stellaris.MODID, "no_oxygen_needed"));
    public static final TagKey<EntityType<?>> ENTITY_PLANET_FIRE_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Stellaris.MODID, "planet_fire"));
    public static final TagKey<EntityType<?>> ENTITY_VENUS_RAIN_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Stellaris.MODID, "venus_rain"));

    public static final TagKey<EntityType<?>> ENTITY_RADIATION_INVULNERABLE_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Stellaris.MODID, "radiations_invulnerable"));

    /** FLUIDS */
    public static final TagKey<Fluid> FLUID_VEHICLE_FUEL_TAG = TagKey.create(Registries.FLUID, new ResourceLocation(Stellaris.MODID, "vehicle_fuel"));
    public static final TagKey<Fluid> FLUID_OIL_FLUID_TAG = TagKey.create(Registries.FLUID, new ResourceLocation(Stellaris.MODID, "oil"));

    /** BIOMES */
    public static final TagKey<Biome> MARS_BIOMES_TAG = TagKey.create(Registries.BIOME, new ResourceLocation(Stellaris.MODID, "mars_biomes"));
    public static final TagKey<Biome> MERCURY_BIOMES_TAG = TagKey.create(Registries.BIOME, new ResourceLocation(Stellaris.MODID, "mercury_biomes"));
    public static final TagKey<Biome> MOON_BIOMES_TAG = TagKey.create(Registries.BIOME, new ResourceLocation(Stellaris.MODID, "moon_biomes"));
    public static final TagKey<Biome> VENUS_BIOMES_TAG = TagKey.create(Registries.BIOME, new ResourceLocation(Stellaris.MODID, "venus_biomes"));

    /** ROCKET UPGRADE */
    public static final TagKey<Item> ROCKET_UPGRADE_TAG = TagKey.create(Registries.ITEM, new ResourceLocation(Stellaris.MODID, "rocket_upgrade"));

    /** ITEMS */
    public static final TagKey<Item> SPACE_FOOD = TagKey.create(Registries.ITEM, new ResourceLocation(Stellaris.MODID, "space_food"));
    public static final TagKey<Item> COAL_GENERATOR_FUEL_TAG = TagKey.create(Registries.ITEM, new ResourceLocation(Stellaris.MODID, "coal_generator_fuel"));
    public static final TagKey<Item> RADIOACTIVE_GENERATOR_FUEL_TAG = TagKey.create(Registries.ITEM, new ResourceLocation(Stellaris.MODID, "radioactive_generator_fuel"));
    public static final TagKey<Item> ROCKET_SKIN = TagKey.create(Registries.ITEM, new ResourceLocation(Stellaris.MODID, "rocket_skin"));

    /** BLOCKS */
    public static final TagKey<Block> SPACE_STATION_CAN_SPAWN_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(Stellaris.MODID, "space_station_can_spawn_on"));

    public static final TagKey<Block> ENERGY_BLOCK_TAG = TagKey.create(Registries.BLOCK, new ResourceLocation(Stellaris.MODID, "energy_block"));
    public static final TagKey<MobEffect> RADIOACTIVEEFFECT = TagKey.create(Registries.MOB_EFFECT, new ResourceLocation(Stellaris.MODID, "radioactive"));

}
