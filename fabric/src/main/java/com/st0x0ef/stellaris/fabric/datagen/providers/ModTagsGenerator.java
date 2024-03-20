package com.st0x0ef.stellaris.fabric.datagen.providers;

import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;


public class ModTagsGenerator  {


    public class ItemTagsGenerator extends FabricTagProvider.ItemTagProvider {
        public ItemTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider tag) {
        }
    }

    public static class BlockTagsGenerator extends FabricTagProvider.BlockTagProvider {
        public BlockTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider tag) {
            getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(BlocksRegistry.RAW_STEEL_BLOCK.get(), BlocksRegistry.STEEL_BLOCK.get(), BlocksRegistry.STEEL_ORE.get(), BlocksRegistry.DEEPSLATE_STEEL_ORE.get());
            getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                    .add(BlocksRegistry.RAW_STEEL_BLOCK.get(), BlocksRegistry.STEEL_BLOCK.get(), BlocksRegistry.STEEL_ORE.get(), BlocksRegistry.DEEPSLATE_STEEL_ORE.get());

        }
    }

}

