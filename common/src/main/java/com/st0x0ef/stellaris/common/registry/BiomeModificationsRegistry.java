package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.world.ModPlacedFeatures;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;


public class BiomeModificationsRegistry {

    public static void register() {
        ResourceKey<PlacedFeature> UNDERGROUND_OIL_LAKE = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Stellaris.MODID,"underground_oil_lake"));

        BiomeModifications.addProperties((context) -> context.hasTag(BiomeTags.IS_OVERWORLD), ((biomeContext, mutable) -> mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.LAKES, UNDERGROUND_OIL_LAKE)));

        /** MARS ORES */
        BiomeModifications.addProperties(
                (context) -> context.hasTag(TagRegistry.MARS_BIOMES_TAG),
                ((biomeContext, mutable) -> {
                    //mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MARS_DIAMOND_PLACED_KEY);
                    //mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MARS_ICE_SHARD_PLACED_KEY);
                    mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MARS_OSTRUM_PLACED_KEY);
                    mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MARS_IRON_PLACED_KEY);
                })
        );

        /** VENUS ORES */
        BiomeModifications.addProperties(
                (context) -> context.hasTag(TagRegistry.VENUS_BIOMES_TAG),
                ((biomeContext, mutable) -> {
                    //mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.VENUS_CALORITE_PLACED_KEY);
                    mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.VENUS_COAL_PLACED_KEY);
                    //mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.VENUS_DIAMOND_PLACED_KEY);
                    mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.VENUS_GOLD_PLACED_KEY);
                })
        );

        /** MOON ORES */
        BiomeModifications.addProperties(
                (context) -> context.hasTag(TagRegistry.MOON_BIOMES_TAG),
                ((biomeContext, mutable) -> {
                    //mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MOON_CHEESE_PLACED_KEY);
                    mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MOON_DESH_PLACED_KEY);
                    //mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MOON_ICE_SHARD_PLACED_KEY);
                    //mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MOON_IRON_PLACED_KEY);
                    mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MOON_SOUL_SOIL_PLACED_KEY);

                })
        );

        /** MERCURY ORES */
        BiomeModifications.addProperties(
                (context) -> context.hasTag(TagRegistry.MERCURY_BIOMES_TAG),
                ((biomeContext, mutable) -> {
                    mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.MERCURY_IRON_PLACED_KEY);
                })
        );

    }
}
