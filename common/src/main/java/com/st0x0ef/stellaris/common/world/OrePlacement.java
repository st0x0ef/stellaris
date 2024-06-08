package com.st0x0ef.stellaris.common.world;

import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OrePlacement {

    public static List<PlacementModifier> orePlacement(PlacementModifier modifier, PlacementModifier modifier2) {
        return List.of(modifier, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int veinsPerChunk, PlacementModifier modifier) {
        return orePlacement(CountPlacement.of(veinsPerChunk), modifier);
    }

    public static List<PlacementModifier> rareOrePlacement(int veinsPerChunk, PlacementModifier modifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(veinsPerChunk), modifier);
    }

}
