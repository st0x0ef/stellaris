package com.st0x0ef.stellaris.client.ModelProvider;


import com.google.gson.JsonElement;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.ModelProvider;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PlayerHandModelProvider extends FabricModelProvider {
    public PlayerHandModelProvider(FabricDataOutput output, BiConsumer<ResourceLocation, Supplier<JsonElement>> output1, BiConsumer<ResourceLocation, Supplier<JsonElement>> output2) {
        super(output);

        this.output = output2;
    }
    public final BiConsumer<ResourceLocation, Supplier<JsonElement>> output;



    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }



    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        this.generateFlatItem(ItemsRegistry.STEEL_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(ItemsRegistry.STEEL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(ItemsRegistry.STEEL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(ItemsRegistry.STEEL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(ItemsRegistry.STEEL_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);


    }

    public final void generateFlatItem(Item item, ModelTemplate modelTemplate) {
        modelTemplate.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), this.output);
    }

}