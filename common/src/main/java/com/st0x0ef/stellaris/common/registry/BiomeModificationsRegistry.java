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


    }
}
