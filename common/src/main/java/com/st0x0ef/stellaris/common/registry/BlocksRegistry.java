package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.FlagBlock;
import com.st0x0ef.stellaris.common.blocks.RocketLaunchPad;
import com.st0x0ef.stellaris.common.blocks.RocketStation;
import com.st0x0ef.stellaris.common.blocks.UraniumBlock;
import dev.architectury.core.block.ArchitecturyLiquidBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class BlocksRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Stellaris.MODID, Registries.BLOCK);

    /** General */
    public static final RegistrySupplier<Block> STEEL_BLOCK = BLOCKS.register("steel_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(4.5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> PERMAFROST_BRICKS = BLOCKS.register("permafrost_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(4.5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> CRACKED_PERMAFROST_BRICKS = BLOCKS.register("cracked_permafrost_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(4.5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> IRON_PLATING_BLOCK = BLOCKS.register("iron_plating_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(4.5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> RAW_STEEL_BLOCK = BLOCKS.register("raw_steel_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> STEEL_PLANTING_BLOCK = BLOCKS.register("steel_planting_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> HEAVY_METAL_PLATE = BLOCKS.register("heavy_metal_plate", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> HEAVY_METAL_CASING = BLOCKS.register("heavy_metal_casing", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> DESH_BLOCK = BLOCKS.register("desh_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> RAW_DESH_BLOCK = BLOCKS.register("raw_desh_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> DESH_PLANTING_BLOCK = BLOCKS.register("desh_plating_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> DESH_PILLAR = BLOCKS.register("desh_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> FLAG = BLOCKS.register("flag", () -> new FlagBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));


    /** Radioactivity */
    public static final RegistrySupplier<Block> MERCURY_URANIUM_ORE = BLOCKS.register("mercury_uranium_ore", () -> new UraniumBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> URANIUM_BLOCK = BLOCKS.register("uranium_block", () -> new UraniumBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> RAW_URANIUM_BLOCK = BLOCKS.register("raw_uranium_block", () -> new UraniumBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));


    /** MARS Blocks */
    public static final RegistrySupplier<Block> CHISELED_MARS_STONE_BRICKS = BLOCKS.register("chiseled_mars_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> CONGLOMORATE = BLOCKS.register("conglomorate", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> CRACKED_MARS_STONE_BRICKS = BLOCKS.register("cracked_mars_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MARS_COBBLESONE = BLOCKS.register("mars_cobblestone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MARS_IRON_ORE = BLOCKS.register("mars_iron_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MARS_OSTRUM_ORE = BLOCKS.register("mars_ostrum_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MARS_SAND = BLOCKS.register("mars_sand", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.SAND).sound(SoundType.SAND).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MARS_STONE = BLOCKS.register("mars_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MARS_STONE_BRICKS = BLOCKS.register("mars_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> POLISHED_CONGLOMORATE = BLOCKS.register("polished_conglomorate", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> POLISHED_MARS_STONE = BLOCKS.register("polished_mars_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));

    /** GLACIO Blocks */
    public static final RegistrySupplier<Block> CHISELED_GLACIO_STONE = BLOCKS.register("chiseled_glacio_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> CRACKED_GLACIO_STONE_BRICKS = BLOCKS.register("cracked_glacio_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> GLACIO_COAL_ORE = BLOCKS.register("glacio_coal_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> GLACIO_COBBLESTONE = BLOCKS.register("glacio_cobblestone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> GLACIO_COPPER_ORE = BLOCKS.register("glacio_copper_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> GLACIO_ICE_SHARD_ORE = BLOCKS.register("glacio_ice_shard_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> GLACIO_IRON_ORE = BLOCKS.register("glacio_iron_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> GLACIO_LAPIS_ORE = BLOCKS.register("glacio_lapis_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> GLACIO_STONE = BLOCKS.register("glacio_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> GLACIO_STONE_BRICKS = BLOCKS.register("glacio_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> POLISHED_GLACIO_STONE = BLOCKS.register("polished_glacio_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));

    /** MERCURY Blocks */
    public static final RegistrySupplier<Block> MERCURY_COBBLESTONE = BLOCKS.register("mercury_cobblestone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MERCURY_IRON_ORE = BLOCKS.register("mercury_iron_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MERCURY_STONE = BLOCKS.register("mercury_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MERCURY_STONE_BRICKS = BLOCKS.register("mercury_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MERCURY_STONE_PILLAR = BLOCKS.register("mercury_stone_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> CHISELED_MERCURY_STONE = BLOCKS.register("chiseled_mercury_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> CRACKED_MERCURY_STONE_BRICKS = BLOCKS.register("cracked_mercury_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> POLISHED_MERCURY_STONE = BLOCKS.register("polished_mercury_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MERCURY_STONE_BRICK_SLAB = BLOCKS.register("mercury_stone_brick_slab", () -> new SlabBlock(SlabBlock.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MERCURY_STONE_BRICK_STAIRS = BLOCKS.register("mercury_stone_brick_stairs", () -> new SlabBlock(SlabBlock.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));


    /** Venus Blocks */
    public static final RegistrySupplier<Block> CHISELED_VENUS_STONE = BLOCKS.register("chiseled_venus_stone", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> CRACKED_VENUS_SANDSTONE_BRICKS = BLOCKS.register("cracked_venus_sandstone_bricks", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> CRACKED_VENUS_STONE_BRICKS = BLOCKS.register("cracked_venus_stone_bricks", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> POLISHED_VENUS_STONE = BLOCKS.register("polished_venus_stone", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> VENUS_COAL_ORE = BLOCKS.register("venus_coal_ore", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> VENUS_COBBLESTONE = BLOCKS.register("venus_cobblestone", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> VENUS_GOLD_ORE = BLOCKS.register("venus_gold_ore", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> VENUS_SAND = BLOCKS.register("venus_sand", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.SAND).sound(SoundType.SAND).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> VENUS_SANDSTONE = BLOCKS.register("venus_sandstone", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> VENUS_SANDSTONE_BRICKS = BLOCKS.register("venus_sandstone_bricks", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> VENUS_STONE = BLOCKS.register("venus_stone", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> VENUS_STONE_BRICKS = BLOCKS.register("venus_stone_bricks", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));

    /** Moon Blocks */
    public static final RegistrySupplier<Block> CHISELED_MOON_STONE = BLOCKS.register("chiseled_moon_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_COBBLESTONE = BLOCKS.register("moon_cobblestone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_SAND = BLOCKS.register("moon_sand", () -> new ColoredFallingBlock(new ColorRGBA(0x7E7E7E), BlockBehaviour.Properties.of().mapColor(MapColor.SAND).sound(SoundType.SAND).strength(0.2f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_STONE = BLOCKS.register("moon_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_DEEPSLATE = BLOCKS.register("moon_deepslate", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_STONE_DUST = BLOCKS.register("moon_stone_dust", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> CRACKED_MOON_STONE_BRICKS = BLOCKS.register("cracked_moon_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_STONE_BRICKS = BLOCKS.register("moon_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_STONE_PILLAR = BLOCKS.register("moon_stone_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> POLISHED_MOON_STONE = BLOCKS.register("polished_moon_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_DESH_ORE = BLOCKS.register("moon_desh_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));

    public static final RegistrySupplier<Block> STEEL_ORE = BLOCKS.register("steel_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistrySupplier<Block> DEEPSLATE_STEEL_ORE = BLOCKS.register("deepslate_steel_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), BlockBehaviour.Properties.ofLegacyCopy(STEEL_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));

    public static final RegistrySupplier<RotatedPillarBlock> INFERNAL_SPIRE = BLOCKS.register("infernal_spire", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 1f).requiresCorrectToolForDrops()));

    /** Machine */
    public static final RegistrySupplier<Block> ROCKET_STATION = BLOCKS.register("rocket_station", () -> new RocketStation(BlockBehaviour.Properties.ofLegacyCopy(STEEL_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));

    public static final RegistrySupplier<Block> ROCKET_LAUNCH_PAD = BLOCKS.register("rocket_launch_pad", () -> new RocketLaunchPad(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));

    /**Fluid Blocks*/
    public static final RegistrySupplier<LiquidBlock> FUEL_BLOCK = BLOCKS.register("fuel", () -> new ArchitecturyLiquidBlock(FluidRegistry.FUEL_STILL, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
    public static final RegistrySupplier<LiquidBlock> OIL_BLOCK = BLOCKS.register("oil", () -> new ArchitecturyLiquidBlock(FluidRegistry.OIL_STILL, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
    public static final RegistrySupplier<LiquidBlock> HYDROGEN_BLOCK = BLOCKS.register("hydrogen", () -> new ArchitecturyLiquidBlock(FluidRegistry.HYDROGEN_STILL, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
}
