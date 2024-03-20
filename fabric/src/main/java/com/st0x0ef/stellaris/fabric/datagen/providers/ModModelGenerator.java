package com.st0x0ef.stellaris.fabric.datagen.providers;

import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.world.level.block.Block;

import static net.minecraft.data.models.BlockModelGenerators.createSimpleBlock;


public class ModModelGenerator extends FabricModelProvider {

    public ModModelGenerator(FabricDataOutput generator) {
        super(generator);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        createAllBlock(blockStateModelGenerator, BlocksRegistry.STEEL_BLOCK.get());
        createAllBlock(blockStateModelGenerator, BlocksRegistry.RAW_STEEL_BLOCK.get());
        createAllBlock(blockStateModelGenerator, BlocksRegistry.STEEL_ORE.get());
        createAllBlock(blockStateModelGenerator, BlocksRegistry.DEEPSLATE_STEEL_ORE.get());

        createAllBlock(blockStateModelGenerator, BlocksRegistry.ROCKET_STATION.get());

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ItemsRegistry.STEEL_NUGGET.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsRegistry.RAW_STEEL_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsRegistry.STEEL_INGOT.get(), ModelTemplates.FLAT_ITEM);

        /** Rocket Parts */
        itemModelGenerator.generateFlatItem(ItemsRegistry.ROCKET_NOSE_CONE.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsRegistry.ROCKET_ENGINE.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsRegistry.ROCKET_FIN.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsRegistry.ROCKET_FAN.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsRegistry.FUEL_ROCKET_MOTOR.get(), ModelTemplates.FLAT_ITEM);

    }


    public void createAllBlock(BlockModelGenerators gen, Block block) {
        TextureMapping textureMapping = (new TextureMapping()).put(TextureSlot.ALL, TextureMapping.getBlockTexture(block));
        gen.blockStateOutput.accept(createSimpleBlock(block, ModelTemplates.CUBE_ALL.create(block, textureMapping, gen.modelOutput)));
    }

}

