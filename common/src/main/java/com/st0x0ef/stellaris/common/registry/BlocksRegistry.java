package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.RocketStation;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class BlocksRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Stellaris.MODID, Registries.BLOCK);

    public static final RegistrySupplier<Block> STEEL_BLOCK = BLOCKS.register("steel_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> RAW_STEEL_BLOCK = BLOCKS.register("raw_steel_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> STEEL_PLANTING_BLOCK = BLOCKS.register("steel_planting_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> HEAVY_METAL_PLATE = BLOCKS.register("heavy_metal_plate", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> HEAVY_METAL_CASING = BLOCKS.register("heavy_metal_casing", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.METAL).strength(5f, 2.5f).requiresCorrectToolForDrops()));



    /** Moon Items Part 1 */
    public static final RegistrySupplier<Block> CHISELED_MOON_STONE = BLOCKS.register("chiseled_moon_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_COBBLESTONE = BLOCKS.register("moon_cobblestone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_SAND = BLOCKS.register("moon_sand", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).sound(SoundType.SAND).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_STONE = BLOCKS.register("moon_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_DEEPSLATE = BLOCKS.register("moon_deepslate", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_STONE_DUST = BLOCKS.register("moon_stone_dust", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));

    /** Moon Items Part 2 */
    public static final RegistrySupplier<Block> CRACKED_MOON_STONE_BRICKS = BLOCKS.register("cracked_moon_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_STONE_BRICKS = BLOCKS.register("moon_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_STONE_PILLAR_END = BLOCKS.register("moon_stone_pillar_end", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> MOON_STONE_PILLAR_SIDE = BLOCKS.register("moon_stone_pillar_side", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> POLISHED_MOON_STONE = BLOCKS.register("polished_moon_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(5f, 2.5f).requiresCorrectToolForDrops()));

    public static final RegistrySupplier<Block> STEEL_ORE = BLOCKS.register("steel_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistrySupplier<Block> DEEPSLATE_STEEL_ORE = BLOCKS.register("deepslate_steel_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), BlockBehaviour.Properties.ofLegacyCopy(STEEL_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));

    /** TODO */
    public static final RegistrySupplier<RotatedPillarBlock> INFERNAL_SPIRE = BLOCKS.register("infernal_spire", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5f, 1f).requiresCorrectToolForDrops()));



    /** Machine */
    public static final RegistrySupplier<Block> ROCKET_STATION = BLOCKS.register("rocket_station", () -> new RocketStation(BlockBehaviour.Properties.ofLegacyCopy(STEEL_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));

}
