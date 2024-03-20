package com.st0x0ef.stellaris.fabric.datagen;

import com.st0x0ef.stellaris.fabric.datagen.providers.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;


public class ModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(ModModelGenerator::new);
        pack.addProvider(ModTagsGenerator.BlockTagsGenerator::new);
        pack.addProvider(ModLootTableGenerator::new);
        pack.addProvider(ModLangGenerator::new);
        pack.addProvider(ModRecipeGenerator::new);
    }



}
