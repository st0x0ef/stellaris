package com.st0x0ef.stellaris.fabric.datagen.providers;

import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;


public class ModLangGenerator extends FabricLanguageProvider {


    public ModLangGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemsRegistry.RAW_STEEL_INGOT.get(), "Raw Steel Ingot");
        translationBuilder.add(ItemsRegistry.RAW_STEEL_BLOCK_ITEM.get(), "Raw Steel Block");
        translationBuilder.add(ItemsRegistry.STEEL_INGOT.get(), "Steel Ingot");
        translationBuilder.add(ItemsRegistry.STEEL_NUGGET.get(), "Steel Nugget");
        translationBuilder.add(ItemsRegistry.STEEL_BLOCK_ITEM.get(), "Steel Block");
        translationBuilder.add(ItemsRegistry.STEEL_ORE_ITEM.get(), "Steel Ore");
        translationBuilder.add(ItemsRegistry.DEEPSLATE_STEEL_ORE_ITEM.get(), "Deepslate Steel Ore");
        translationBuilder.add("categorie.stellaris.main", "Stellaris");

    }
}

