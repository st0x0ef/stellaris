package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public class ItemsRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Stellaris.MODID, Registries.ITEM);

    public static final RegistrySupplier<Item> ICE_SHARD = ITEMS.register("ice_shard", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> STEEL_NUGGET = ITEMS.register("steel_nugget", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> RAW_STEEL_INGOT = ITEMS.register("raw_steel_ingot", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));

    /** Block Items */
    public static final RegistrySupplier<Item> STEEL_BLOCK_ITEM = ITEMS.register("steel_block", () -> new BlockItem(BlocksRegistry.STEEL_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> RAW_STEEL_BLOCK_ITEM = ITEMS.register("raw_steel_block", () -> new BlockItem(BlocksRegistry.RAW_STEEL_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> STEEL_PLANTING_BLOCK_ITEM = ITEMS.register("steel_planting_block", () -> new BlockItem(BlocksRegistry.STEEL_PLANTING_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));

    public static final RegistrySupplier<Item> STEEL_ORE_ITEM = ITEMS.register("steel_ore", () -> new BlockItem(BlocksRegistry.STEEL_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> DEEPSLATE_STEEL_ORE_ITEM = ITEMS.register("deepslate_steel_ore", () -> new BlockItem(BlocksRegistry.DEEPSLATE_STEEL_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));

    public static final RegistrySupplier<Item> HEAVY_METAL_PLATE_ITEM = ITEMS.register("heavy_metal_plate", () -> new BlockItem(BlocksRegistry.HEAVY_METAL_PLATE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> HEAVY_METAL_CASING_ITEM = ITEMS.register("heavy_metal_casing", () -> new BlockItem(BlocksRegistry.HEAVY_METAL_CASING.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));

    /** Moon Items 1*/
    public static final RegistrySupplier<Item> CHISELED_MOON_STONE_ITEM = ITEMS.register("chiseled_moon_stone", () -> new BlockItem(BlocksRegistry.CHISELED_MOON_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MOON_COBBLESTONE_ITEM = ITEMS.register("moon_cobblestone", () -> new BlockItem(BlocksRegistry.MOON_COBBLESTONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MOON_SAND_ITEM = ITEMS.register("moon_sand", () -> new BlockItem(BlocksRegistry.MOON_SAND.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_ITEM = ITEMS.register("moon_stone", () -> new BlockItem(BlocksRegistry.MOON_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MOON_DEEPSLATE_ITEM = ITEMS.register("moon_deepslate", () -> new BlockItem(BlocksRegistry.MOON_DEEPSLATE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_DUST_ITEM = ITEMS.register("moon_stone_dust", () -> new BlockItem(BlocksRegistry.MOON_STONE_DUST.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));

    /** Moon Items 2*/
    public static final RegistrySupplier<Item> CRACKED_MOON_STONE_BRICKS_ITEM = ITEMS.register("cracked_moon_stone_bricks", () -> new BlockItem(BlocksRegistry.CRACKED_MOON_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_BRICKS_ITEM = ITEMS.register("moon_stone_bricks", () -> new BlockItem(BlocksRegistry.MOON_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_PILLAR_END_ITEM = ITEMS.register("moon_stone_pillar_end", () -> new BlockItem(BlocksRegistry.MOON_STONE_PILLAR_END.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_PILLAR_SIDE_ITEM = ITEMS.register("moon_stone_pillar_side", () -> new BlockItem(BlocksRegistry.MOON_STONE_PILLAR_SIDE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> POLISHED_MOON_STONE_ITEM = ITEMS.register("polished_moon_stone", () -> new BlockItem(BlocksRegistry.POLISHED_MOON_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));

    /** Rocket Parts */
    public static final RegistrySupplier<Item> ROCKET_NOSE_CONE = ITEMS.register("rocket_nose_cone", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> ROCKET_FIN = ITEMS.register("rocket_fin", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> ROCKET_FAN = ITEMS.register("engine_fan", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> ROCKET_ENGINE = ITEMS.register("rocket_engine", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> FUEL_ROCKET_MOTOR = ITEMS.register("fuel_rocket_motor", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));


    public static final RegistrySupplier<Item> ROCKET_STATION = ITEMS.register("rocket_station", () -> new BlockItem(BlocksRegistry.ROCKET_STATION.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));


    /** Mob Eggs */

    public static final RegistrySupplier<SpawnEggItem> ALIEN_SPAWN_EGG = ITEMS.register("alien_spawn_egg",
            ()-> new SpawnEggItem(EntityRegistry.ALIEN.get(), 0xc4c4c4, 0xadadad, new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<SpawnEggItem> ALIEN_ZOMBIE_SPAWN_EGG = ITEMS.register("alien_zombie_spawn_egg",
            ()-> new SpawnEggItem(EntityRegistry.ALIEN_ZOMBIE.get(), 0xc4c4c4, 0xadadad, new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<SpawnEggItem> PYGRO_SPAWN_EGG = ITEMS.register("pygro_spawn_egg",
            ()-> new SpawnEggItem(EntityRegistry.PYGRO.get(), 0xc4c4c4, 0xadadad, new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<SpawnEggItem> PYGRO_BRUTE_SPAWN_EGG = ITEMS.register("pygro_brute_spawn_egg",
            ()-> new SpawnEggItem(EntityRegistry.PYGRO_BRUTE.get(), 0xc4c4c4, 0xadadad, new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<SpawnEggItem> MARTIAN_RAPTOR_SPAWN_EGG = ITEMS.register("martian_raptor_spawn_egg",
            ()-> new SpawnEggItem(EntityRegistry.MARTIAN_RAPTOR.get(), 0xc4c4c4, 0xadadad, new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<SpawnEggItem> STAR_CRAWLER_SPAWN_EGG = ITEMS.register("star_crawler_spawn_egg",
            ()-> new SpawnEggItem(EntityRegistry.STAR_CRAWLER.get(), 0xc4c4c4, 0xadadad, new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<SpawnEggItem> MOGLER_SPAWN_EGG = ITEMS.register("mogler_spawn_egg",
            ()-> new SpawnEggItem(EntityRegistry.MOGLER.get(), 0xc4c4c4, 0xadadad, new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
}