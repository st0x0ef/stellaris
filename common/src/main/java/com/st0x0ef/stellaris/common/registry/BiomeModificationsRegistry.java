package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.world.ModPlacedFeatures;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;


public class BiomeModificationsRegistry {

    public static void register() {

        BiomeModifications.addProperties((context) -> context.hasTag(BiomeTags.IS_OVERWORLD), ((biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, ModPlacedFeatures.LAKE_OIL_SURFACE);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.STEEL_ORE_PLACED_KEY);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.STEEL_ORE_DEEPSLATE_PLACED_KEY);

            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacedFeatures.LAKE_OIL_UNDERGROUND);
        }));

        BiomeModifications.addProperties((context) -> context.hasTag(TagRegistry.MOON_BIOMES_TAG), ((biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MOON_DESH_PLACED_KEY);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MOON_STEEL_PLACED_KEY);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MOON_ICE_SHARD_PLACED_KEY);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MOON_IRON_PLACED_KEY);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MOON_SOUL_SOIL_PLACED_KEY);
        }));

        BiomeModifications.addProperties((context) -> context.hasTag(TagRegistry.MARS_BIOMES_TAG), ((biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MARS_DIAMOND_PLACED_KEY);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MARS_ICE_SHARD_PLACED_KEY);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MARS_IRON_PLACED_KEY);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MARS_OSTRUM_PLACED_KEY);
        }));

        BiomeModifications.addProperties((context) -> context.hasTag(TagRegistry.MERCURY_BIOMES_TAG), ((biomeContext, mutable) ->
                mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MERCURY_IRON_PLACED_KEY)));

        BiomeModifications.addProperties((context) -> context.hasTag(TagRegistry.VENUS_BIOMES_TAG), ((biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.VENUS_COAL_PLACED_KEY);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.VENUS_DIAMOND_PLACED_KEY);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.VENUS_GOLD_PLACED_KEY);
        }));
    }
}
