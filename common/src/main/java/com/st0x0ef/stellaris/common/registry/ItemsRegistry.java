package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.items.CanItem;
import com.st0x0ef.stellaris.common.items.IceShardArrow;
import com.st0x0ef.stellaris.common.items.RadioactiveBlockItem;
import com.st0x0ef.stellaris.common.items.RadioactiveItem;
import dev.architectury.core.item.ArchitecturyBucketItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;

public class ItemsRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Stellaris.MODID, Registries.ITEM);


    /** ITEMS */
    public static final RegistrySupplier<Item> ICE_SHARD = ITEMS.register("ice_shard", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> STEEL_NUGGET = ITEMS.register("steel_nugget", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> STEEL_ORE_ITEM = ITEMS.register("steel_ore", () -> new BlockItem(BlocksRegistry.STEEL_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> DEEPSLATE_STEEL_ORE_ITEM = ITEMS.register("deepslate_steel_ore", () -> new BlockItem(BlocksRegistry.DEEPSLATE_STEEL_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> HEAVY_METAL_INGOT_ITEM = ITEMS.register("heavy_metal_ingot", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> HEAVY_METAL_NUGGET_ITEM = ITEMS.register("heavy_metal_nugget", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> RAW_STEEL_INGOT = ITEMS.register("raw_steel_ingot", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    //** Food Cans */
    public static final RegistrySupplier<Item> BIG_BLUE_CAN = ITEMS.register("big_blue_can", () -> new CanItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 20));
    public static final RegistrySupplier<Item> BIG_GREEN_CAM = ITEMS.register("big_green_can", () -> new CanItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 20));
    public static final RegistrySupplier<Item> BIG_PURPLE_CAN = ITEMS.register("big_purple_can", () -> new CanItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 20));
    public static final RegistrySupplier<Item> BIG_RED_CAN = ITEMS.register("big_red_can", () -> new CanItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 20));
    public static final RegistrySupplier<Item> BIG_YELLOW_CAN = ITEMS.register("big_yellow_can", () -> new CanItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 20));
    public static final RegistrySupplier<Item> SMALL_BLUE_CAN = ITEMS.register("small_blue_can", () -> new CanItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 10));
    public static final RegistrySupplier<Item> SMALL_GREEN_CAN = ITEMS.register("small_green_can", () -> new CanItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 10));
    public static final RegistrySupplier<Item> SMALL_PURPLE_CAN = ITEMS.register("small_purple_can", () -> new CanItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 10));
    public static final RegistrySupplier<Item> SMALL_RED_CAN = ITEMS.register("small_red_can", () -> new CanItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 10));
    public static final RegistrySupplier<Item> SMALL_YELLOW_CAN = ITEMS.register("small_yellow_can", () -> new CanItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 10));
    //** Food Items */

    public static final RegistrySupplier<Item> CANNON_STEAK = ITEMS.register("cannon_steak", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB).food(FoodRegistry.CANNED_STEAK)));
    public static final RegistrySupplier<Item> COSMO_BREAD = ITEMS.register("cosmo_bread", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB).food(FoodRegistry.COSMO_BREAD)));
    public static final RegistrySupplier<Item> BERRY_JUICE = ITEMS.register("berry_juice", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB).food(FoodRegistry.BERRY_JUICE)));
    public static final RegistrySupplier<Item> CHEESE = ITEMS.register("cheese", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB).food(FoodRegistry.CHEESE)));

    /** BLOCKS ITEMS */
    public static final RegistrySupplier<Item> STEEL_BLOCK_ITEM = ITEMS.register("steel_block", () -> new BlockItem(BlocksRegistry.STEEL_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> RAW_STEEL_BLOCK_ITEM = ITEMS.register("raw_steel_block", () -> new BlockItem(BlocksRegistry.RAW_STEEL_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> STEEL_PLANTING_BLOCK_ITEM = ITEMS.register("steel_planting_block", () -> new BlockItem(BlocksRegistry.STEEL_PLANTING_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> STEEL_PILLAR_ITEM = ITEMS.register("steel_pillar", () -> new BlockItem(BlocksRegistry.STEEL_PILLAR.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> STEEL_STAIRS_ITEM = ITEMS.register("steel_stairs", () -> new BlockItem(BlocksRegistry.STEEL_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> HEAVY_METAL_PLATE_ITEM = ITEMS.register("heavy_metal_plate", () -> new BlockItem(BlocksRegistry.HEAVY_METAL_PLATE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> HEAVY_METAL_CASING_ITEM = ITEMS.register("heavy_metal_casing", () -> new BlockItem(BlocksRegistry.HEAVY_METAL_CASING.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> HEAVY_METAL_STAIRS_ITEM = ITEMS.register("heavy_metal_stairs", () -> new BlockItem(BlocksRegistry.HEAVY_METAL_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> IRON_PLATING_BLOCK_ITEM = ITEMS.register("iron_plating_block", () -> new BlockItem(BlocksRegistry.IRON_PLATING_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> IRON_PILLAR_ITEM = ITEMS.register("iron_pillar", () -> new BlockItem(BlocksRegistry.IRON_PILLAR.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> IRON_PLATING_STAIRS_ITEM = ITEMS.register("iron_plating_stairs", () -> new BlockItem(BlocksRegistry.IRON_PLATING_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> IRON_PLATING_SLAB_ITEM = ITEMS.register("iron_plating_slab", () -> new BlockItem(BlocksRegistry.IRON_PLATING_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> CONGLOMORATE = ITEMS.register("conglomorate", () -> new BlockItem(BlocksRegistry.CONGLOMORATE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> POLISHED_CONGLOMORATE = ITEMS.register("polished_conglomorate", () -> new BlockItem(BlocksRegistry.POLISHED_CONGLOMORATE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> CONGLOMORATE_STAIRS_ITEM = ITEMS.register("conglomorate_stairs", () -> new BlockItem(BlocksRegistry.CONGLOMORATE_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> POLISHED_CONGLOMORATE_STAIRS_ITEM = ITEMS.register("polished_conglomorate_stairs", () -> new BlockItem(BlocksRegistry.POLISHED_CONGLOMORATE_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> CONGLOMORATE_SLAB_ITEM = ITEMS.register("conglomorate_slab", () -> new BlockItem(BlocksRegistry.CONGLOMORATE_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> POLISHED_CONGLOMORATE_SLAB_ITEM = ITEMS.register("polished_conglomorate_slab", () -> new BlockItem(BlocksRegistry.POLISHED_CONGLOMORATE_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));


    //Globes
    public static final RegistrySupplier<Item> EARTH_GLOBE_ITEM = ITEMS.register("earth_globe", () -> new BlockItem(BlocksRegistry.EARTH_GLOBE_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MOON_GLOBE_ITEM = ITEMS.register("moon_globe", () -> new BlockItem(BlocksRegistry.MOON_GLOBE_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MARS_GLOBE_ITEM = ITEMS.register("mars_globe", () -> new BlockItem(BlocksRegistry.MARS_GLOBE_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_GLOBE_ITEM = ITEMS.register("mercury_globe", () -> new BlockItem(BlocksRegistry.MERCURY_GLOBE_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> VENUS_GLOBE_ITEM = ITEMS.register("venus_globe", () -> new BlockItem(BlocksRegistry.VENUS_GLOBE_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));

    //RADIOACTIVITY
    public static final RegistrySupplier<Item> MERCURY_URANIUM_ORE_ITEM = ITEMS.register("mercury_uranium_ore", () -> new RadioactiveBlockItem(BlocksRegistry.MERCURY_URANIUM_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 1));
    public static final RegistrySupplier<Item> URANIUM_BLOCK_ITEM = ITEMS.register("uranium_block", () -> new RadioactiveBlockItem(BlocksRegistry.URANIUM_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 1));
    public static final RegistrySupplier<Item> RAW_URANIUM_BLOCK_ITEM = ITEMS.register("raw_uranium_block", () -> new RadioactiveBlockItem(BlocksRegistry.RAW_URANIUM_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 1));
    public static final RegistrySupplier<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot", () -> new RadioactiveItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 1));
    public static final RegistrySupplier<Item> RAW_URANIUM = ITEMS.register("raw_uranium", () -> new RadioactiveItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 1));
    public static final RegistrySupplier<Item> NEPTUNIUM_BLOCK = ITEMS.register("neptunium_block", () -> new RadioactiveBlockItem(BlocksRegistry.NEPTUNIUM_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 1));
    public static final RegistrySupplier<Item> NEPTUNIUM_PIECE = ITEMS.register("neptunium_piece", () -> new RadioactiveItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 1));
    public static final RegistrySupplier<Item> RAW_NEPTUNIUM = ITEMS.register("raw_neptunium", () -> new RadioactiveItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 1));
    public static final RegistrySupplier<Item> NEPTUNIUM_INGOT = ITEMS.register("neptunium_ingot", () -> new RadioactiveItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 1));
    public static final RegistrySupplier<Item> PLUTONIUM_BLOCK = ITEMS.register("plutonium_block", () -> new RadioactiveBlockItem(BlocksRegistry.PLUTONIUM_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 2));
    public static final RegistrySupplier<Item> PLUTONIUM_GRAIN = ITEMS.register("plutonium_grain", () -> new RadioactiveItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 2));
    public static final RegistrySupplier<Item> PLUTONIUM_PIECE = ITEMS.register("plutonium_piece", () -> new RadioactiveItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 2));
    public static final RegistrySupplier<Item> PLUTONIUM_NUGGET = ITEMS.register("plutonium_nugget", () -> new RadioactiveItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 2));
    public static final RegistrySupplier<Item> PLUTONIUM_INGOT = ITEMS.register("plutonium_ingot", () -> new RadioactiveItem(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB), 2));


    /** Mars Items */
    public static final RegistrySupplier<Item> CHISELED_MARS_STONE_BRICKS = ITEMS.register("chiseled_mars_stone_bricks", () -> new BlockItem(BlocksRegistry.CHISELED_MARS_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> CRACKED_MARS_STONE_BRICKS = ITEMS.register("cracked_mars_stone_bricks", () -> new BlockItem(BlocksRegistry.CRACKED_MARS_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_COBBLESONE = ITEMS.register("mars_cobblestone", () -> new BlockItem(BlocksRegistry.MARS_COBBLESONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_IRON_ORE = ITEMS.register("mars_iron_ore", () -> new BlockItem(BlocksRegistry.MARS_IRON_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_OSTRUM_ORE = ITEMS.register("mars_ostrum_ore", () -> new BlockItem(BlocksRegistry.MARS_OSTRUM_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_SAND = ITEMS.register("mars_sand", () -> new BlockItem(BlocksRegistry.MARS_SAND.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_STONE = ITEMS.register("mars_stone", () -> new BlockItem(BlocksRegistry.MARS_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_STONE_BRICKS = ITEMS.register("mars_stone_bricks", () -> new BlockItem(BlocksRegistry.MARS_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_PILLAR_ITEM = ITEMS.register("mars_pillar", () -> new BlockItem(BlocksRegistry.MARS_PILLAR.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> POLISHED_MARS_STONE = ITEMS.register("polished_mars_stone", () -> new BlockItem(BlocksRegistry.POLISHED_MARS_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_STONE_BRICK_STAIRS_ITEM = ITEMS.register("mars_stone_brick_stairs", () -> new BlockItem(BlocksRegistry.MARS_STONE_BRICK_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_POLISHED_STONE_STAIRS_ITEM = ITEMS.register("mars_polished_stone_stairs", () -> new BlockItem(BlocksRegistry.MARS_POLISHED_STONE_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_STONE_STAIRS_ITEM = ITEMS.register("mars_stone_stairs", () -> new BlockItem(BlocksRegistry.MARS_STONE_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_STONE_BRICK_SLAB_ITEM = ITEMS.register("mars_stone_brick_slab", () -> new BlockItem(BlocksRegistry.MARS_STONE_BRICK_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_POLISHED_STONE_SLAB_ITEM = ITEMS.register("mars_polished_stone_slab", () -> new BlockItem(BlocksRegistry.MARS_POLISHED_STONE_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MARS_STONE_SLAB_ITEM = ITEMS.register("mars_stone_slab", () -> new BlockItem(BlocksRegistry.MARS_STONE_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));

    /** Venus Items */
    public static final RegistrySupplier<Item> CHISELED_VENUS_STONE_ITEM = ITEMS.register("chiseled_venus_stone", () -> new BlockItem(BlocksRegistry.CHISELED_VENUS_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> CRACKED_VENUS_SANDSTONE_BRICKS_ITEM = ITEMS.register("cracked_venus_sandstone_bricks", () -> new BlockItem(BlocksRegistry.CRACKED_VENUS_SANDSTONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> CRACKED_VENUS_STONE_BRICKS_ITEM = ITEMS.register("cracked_venus_stone_bricks", () -> new BlockItem(BlocksRegistry.CRACKED_VENUS_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> POLISHED_VENUS_STONE_ITEM = ITEMS.register("polished_venus_stone", () -> new BlockItem(BlocksRegistry.POLISHED_VENUS_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_COAL_ORE_ITEM = ITEMS.register("venus_coal_ore", () -> new BlockItem(BlocksRegistry.VENUS_COAL_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> CHISELED_MOON_STONE_ITEM_ITEM = ITEMS.register("venus_cobblestone", () -> new BlockItem(BlocksRegistry.VENUS_COBBLESTONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_COBBLESTONE_ITEM = ITEMS.register("venus_gold_ore", () -> new BlockItem(BlocksRegistry.VENUS_GOLD_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_SAND_ITEM = ITEMS.register("venus_sand", () -> new BlockItem(BlocksRegistry.VENUS_SAND.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_SANDSTONE_ITEM = ITEMS.register("venus_sandstone", () -> new BlockItem(BlocksRegistry.VENUS_SANDSTONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_SANDSTONE_BRICKS_ITEM = ITEMS.register("venus_sandstone_bricks", () -> new BlockItem(BlocksRegistry.VENUS_SANDSTONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_STONE_ITEM = ITEMS.register("venus_stone", () -> new BlockItem(BlocksRegistry.VENUS_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_STONE_BRICKS_ITEM = ITEMS.register("venus_stone_bricks", () -> new BlockItem(BlocksRegistry.VENUS_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_PILLAR_ITEM = ITEMS.register("venus_pillar", () -> new BlockItem(BlocksRegistry.VENUS_PILLAR.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_SMOOTH_SANDSTONE_BRICK_ITEM = ITEMS.register("venus_smooth_sandstone_brick", () -> new BlockItem(BlocksRegistry.VENUS_SMOOTH_SANDSTONE_BRICK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_STONE_BRICK_STAIRS_ITEM = ITEMS.register("venus_stone_brick_stairs", () -> new BlockItem(BlocksRegistry.VENUS_STONE_BRICK_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_POLISHED_STONE_STAIRS_ITEM = ITEMS.register("venus_polished_stone_stairs", () -> new BlockItem(BlocksRegistry.VENUS_POLISHED_STONE_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_SANDSTONE_BRICKS_STAIRS_ITEM = ITEMS.register("venus_sandstone_brick_stairs", () -> new BlockItem(BlocksRegistry.VENUS_SANDSTONE_BRICKS_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_SMOOTH_SANDSTONE_BRICK_STAIRS_ITEM = ITEMS.register("venus_smooth_sandstone_brick_stairs", () -> new BlockItem(BlocksRegistry.VENUS_SMOOTH_SANDSTONE_BRICK_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_STONE_STAIRS_ITEM = ITEMS.register("venus_stone_stairs", () -> new BlockItem(BlocksRegistry.VENUS_STONE_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_STONE_BRICK_SLAB_ITEM = ITEMS.register("venus_stone_brick_slab", () -> new BlockItem(BlocksRegistry.VENUS_STONE_BRICK_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_POLISHED_STONE_SLAB_ITEM = ITEMS.register("venus_polished_stone_slab", () -> new BlockItem(BlocksRegistry.VENUS_POLISHED_STONE_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_SANDSTONE_BRICK_SLAB_ITEM = ITEMS.register("venus_sandstone_brick_slab", () -> new BlockItem(BlocksRegistry.VENUS_SANDSTONE_BRICK_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_SMOOTH_SANDSTONE_BRICK_SLAB_ITEM = ITEMS.register("venus_smooth_sandstone_brick_slab", () -> new BlockItem(BlocksRegistry.VENUS_SMOOTH_SANDSTONE_BRICK_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> VENUS_STONE_SLAB_ITEM = ITEMS.register("venus_stone_slab", () -> new BlockItem(BlocksRegistry.VENUS_STONE_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));

    //Desh
    public static final RegistrySupplier<Item> MOON_DESH_ORE_ITEM = ITEMS.register("moon_desh_ore", () -> new BlockItem(BlocksRegistry.MOON_DESH_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> DESH_BLOCK_ITEM = ITEMS.register("desh_block", () -> new BlockItem(BlocksRegistry.DESH_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> RAW_DESH_BLOCK_ITEM = ITEMS.register("raw_desh_block", () -> new BlockItem(BlocksRegistry.RAW_DESH_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> DESH_PLANTING_BLOCK_ITEM = ITEMS.register("desh_plating_block", () -> new BlockItem(BlocksRegistry.DESH_PLANTING_BLOCK.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> DESH_PILLAR_ITEM = ITEMS.register("desh_pillar", () -> new BlockItem(BlocksRegistry.DESH_PILLAR.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));


    /** Moon Items */
    public static final RegistrySupplier<Item> CHISELED_MOON_STONE_ITEM = ITEMS.register("chiseled_moon_stone", () -> new BlockItem(BlocksRegistry.CHISELED_MOON_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_COBBLESTONE_ITEM = ITEMS.register("moon_cobblestone", () -> new BlockItem(BlocksRegistry.MOON_COBBLESTONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_SAND_ITEM = ITEMS.register("moon_sand", () -> new BlockItem(BlocksRegistry.MOON_SAND.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_ITEM = ITEMS.register("moon_stone", () -> new BlockItem(BlocksRegistry.MOON_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_DEEPSLATE_ITEM = ITEMS.register("moon_deepslate", () -> new BlockItem(BlocksRegistry.MOON_DEEPSLATE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_DUST_ITEM = ITEMS.register("moon_stone_dust", () -> new BlockItem(BlocksRegistry.MOON_STONE_DUST.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> CRACKED_MOON_STONE_BRICKS_ITEM = ITEMS.register("cracked_moon_stone_bricks", () -> new BlockItem(BlocksRegistry.CRACKED_MOON_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_BRICKS_ITEM = ITEMS.register("moon_stone_bricks", () -> new BlockItem(BlocksRegistry.MOON_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_PILLAR_ITEM = ITEMS.register("moon_stone_pillar", () -> new BlockItem(BlocksRegistry.MOON_STONE_PILLAR.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_PILLAR_ITEM = ITEMS.register("moon_pillar", () -> new BlockItem(BlocksRegistry.MOON_PILLAR.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> POLISHED_MOON_STONE_ITEM = ITEMS.register("polished_moon_stone", () -> new BlockItem(BlocksRegistry.POLISHED_MOON_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_BRICK_STAIRS_ITEM = ITEMS.register("moon_stone_brick_stairs", () -> new BlockItem(BlocksRegistry.MOON_STONE_BRICK_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_STAIRS_ITEM = ITEMS.register("moon_stone_stairs", () -> new BlockItem(BlocksRegistry.MOON_STONE_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_POLISHED_STONE_BRICK_STAIRS_ITEM = ITEMS.register("moon_polished_stone_brick_stairs", () -> new BlockItem(BlocksRegistry.MOON_POLISHED_STONE_BRICK_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_BRICK_SLAB_ITEM = ITEMS.register("moon_stone_brick_slab", () -> new BlockItem(BlocksRegistry.MOON_STONE_BRICK_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_STONE_SLAB_ITEM = ITEMS.register("moon_stone_slab", () -> new BlockItem(BlocksRegistry.MOON_STONE_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MOON_POLISHED_STONE_BRICK_SLAB_ITEM = ITEMS.register("moon_polished_stone_brick_slab", () -> new BlockItem(BlocksRegistry.MOON_POLISHED_STONE_BRICK_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));

    /** Mercury */
    public static final RegistrySupplier<Item> MERCURY_COBBLESTONE_ITEM = ITEMS.register("mercury_cobblestone", () -> new BlockItem(BlocksRegistry.MERCURY_COBBLESTONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_IRON_ORE_ITEM = ITEMS.register("mercury_iron_ore", () -> new BlockItem(BlocksRegistry.MERCURY_IRON_ORE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_STONE_ITEM = ITEMS.register("mercury_stone", () -> new BlockItem(BlocksRegistry.MERCURY_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_STONE_BRICKS_ITEM = ITEMS.register("mercury_stone_bricks", () -> new BlockItem(BlocksRegistry.MERCURY_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_STONE_PILLAR_ITEM = ITEMS.register("mercury_stone_pillar", () -> new BlockItem(BlocksRegistry.MERCURY_STONE_PILLAR.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> CHISELED_MERCURY_STONE_ITEM = ITEMS.register("chiseled_mercury_stone", () -> new BlockItem(BlocksRegistry.CHISELED_MERCURY_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> CRACKED_MERCURY_STONE_BRICKS_ITEM = ITEMS.register("cracked_mercury_stone_bricks", () -> new BlockItem(BlocksRegistry.CRACKED_MERCURY_STONE_BRICKS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> POLISHED_MERCURY_STONE_ITEM = ITEMS.register("polished_mercury_stone", () -> new BlockItem(BlocksRegistry.POLISHED_MERCURY_STONE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_STONE_BRICK_STAIRS_ITEM = ITEMS.register("mercury_stone_brick_stairs", () -> new BlockItem(BlocksRegistry.MERCURY_STONE_BRICK_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_POLISHED_STONE_STAIRS_ITEM = ITEMS.register("mercury_polished_stone_stairs", () -> new BlockItem(BlocksRegistry.MERCURY_POLISHED_STONE_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_STONE_STAIRS_ITEM = ITEMS.register("mercury_stone_stairs", () -> new BlockItem(BlocksRegistry.MERCURY_STONE_STAIRS.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_STONE_BRICK_SLAB_ITEM = ITEMS.register("mercury_stone_brick_slab", () -> new BlockItem(BlocksRegistry.MERCURY_STONE_BRICK_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_POLISHED_STONE_SLAB_ITEM = ITEMS.register("marcury_polished_stone_slab", () -> new BlockItem(BlocksRegistry.MERCURY_POLISHED_STONE_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));
    public static final RegistrySupplier<Item> MERCURY_STONE_SLAB_ITEM = ITEMS.register("marcury_stone_slab", () -> new BlockItem(BlocksRegistry.MERCURY_STONE_SLAB.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));

    public static final RegistrySupplier<Item> METEORITE_ITEM = ITEMS.register("meteorite", () -> new BlockItem(BlocksRegistry.METEORITE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_BLOCKS_TAB)));

    /** Rocket Parts */

    public static final RegistrySupplier<Item> ROCKET_NOSE_CONE = ITEMS.register("rocket_nose_cone", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> ROCKET_FIN = ITEMS.register("rocket_fin", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> ROCKET_FAN = ITEMS.register("engine_fan", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> ROCKET_ENGINE = ITEMS.register("rocket_engine", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> FUEL_ROCKET_MOTOR = ITEMS.register("fuel_rocket_motor", () -> new Item(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));



    public static final RegistrySupplier<Item> ROCKET_STATION = ITEMS.register("rocket_station", () -> new BlockItem(BlocksRegistry.ROCKET_STATION.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> ROCKET_LAUNCH_PAD = ITEMS.register("rocket_launch_pad", () -> new BlockItem(BlocksRegistry.ROCKET_LAUNCH_PAD.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> SOLAR_PANEL = ITEMS.register("solar_panel", () -> new BlockItem(BlocksRegistry.SOLAR_PANEL.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> CABLE = ITEMS.register("cable", () -> new BlockItem(BlocksRegistry.CABLE.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> COAL_GENERATOR = ITEMS.register("coal_generator", () -> new BlockItem(BlocksRegistry.COAL_GENERATOR.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));


    public static final RegistrySupplier<Item> VACUMATOR = ITEMS.register("vacumator", () -> new BlockItem(BlocksRegistry.VACUMATOR.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));


    public static final RegistrySupplier<Item> ICE_SHARD_ARROW = ITEMS.register("ice_shard_arrow", () -> new IceShardArrow(new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));

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

    /**Bucket Items*/
    public static final RegistrySupplier<Item> FUEL_BUCKET = ITEMS.register("fuel_bucket", () -> new ArchitecturyBucketItem(FluidRegistry.FUEL_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> OIL_BUCKET = ITEMS.register("oil_bucket", () -> new ArchitecturyBucketItem(FluidRegistry.OIL_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
    public static final RegistrySupplier<Item> HYDROGEN_BUCKET = ITEMS.register("hydrogen_bucket", () -> new ArchitecturyBucketItem(FluidRegistry.HYDROGEN_STILL, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));

    public static final RegistrySupplier<Item> FLAG_ITEM = ITEMS.register("flag", () -> new BlockItem(BlocksRegistry.FLAG.get(), new Item.Properties().arch$tab(CreativeTabsRegistry.STELLARIS_TAB)));
}