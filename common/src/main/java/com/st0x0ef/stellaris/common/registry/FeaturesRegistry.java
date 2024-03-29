package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.features.InfernalSpireColumn;
import com.st0x0ef.stellaris.common.features.ModifiedBlockBlobFeature;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;

public class FeaturesRegistry {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Stellaris.MODID, Registries.FEATURE);


    public static final RegistrySupplier<InfernalSpireColumn> INFERNAL_SPIRE_COLUMN = FEATURES.register("infernal_spire_column", () -> new InfernalSpireColumn(ColumnFeatureConfiguration.CODEC));
    public static final RegistrySupplier<ModifiedBlockBlobFeature> MODIFIED_BLOCK_BLOB = FEATURES.register("modified_block_blob", () -> new ModifiedBlockBlobFeature(BlockStateConfiguration.CODEC));

}
