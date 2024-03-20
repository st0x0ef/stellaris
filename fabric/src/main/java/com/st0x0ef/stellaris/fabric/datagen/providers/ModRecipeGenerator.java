package com.st0x0ef.stellaris.fabric.datagen.providers;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.List;


public class ModRecipeGenerator extends FabricRecipeProvider {


    public ModRecipeGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {

        customNineBlockStorageRecipes(exporter, RecipeCategory.MISC, ItemsRegistry.RAW_STEEL_INGOT.get(), RecipeCategory.MISC, ItemsRegistry.RAW_STEEL_BLOCK_ITEM.get());
        customNineBlockStorageRecipes(exporter, RecipeCategory.MISC, ItemsRegistry.STEEL_INGOT.get(), RecipeCategory.MISC, ItemsRegistry.STEEL_BLOCK_ITEM.get());

        customNineBlockStorageRecipes(exporter, RecipeCategory.MISC, ItemsRegistry.STEEL_NUGGET.get(), RecipeCategory.MISC, ItemsRegistry.STEEL_INGOT.get());
        oreSmelting(exporter, List.of(ItemsRegistry.DEEPSLATE_STEEL_ORE_ITEM.get(), ItemsRegistry.STEEL_ORE_ITEM.get(), ItemsRegistry.RAW_STEEL_INGOT.get()), RecipeCategory.MISC, ItemsRegistry.STEEL_INGOT.get(), 0.7F, 200, getItemName(ItemsRegistry.STEEL_INGOT.get()));

        /** Rocket Parts */
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistry.ROCKET_NOSE_CONE.get())
                .define('X', Items.REDSTONE_TORCH).define('#', ItemsRegistry.STEEL_INGOT.get())
                .pattern(" X ")
                .pattern(" # ")
                .pattern("###")
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(ItemsRegistry.STEEL_INGOT.get())).save(exporter, new ResourceLocation(getItemName(ItemsRegistry.ROCKET_NOSE_CONE.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistry.ROCKET_FIN.get())
                .define('X', Items.REDSTONE).define('#', ItemsRegistry.STEEL_INGOT.get())
                .pattern(" # ")
                .pattern("#X#")
                .pattern("# #")
                .unlockedBy(getHasName(Items.REDSTONE), has(ItemsRegistry.STEEL_INGOT.get())).save(exporter, new ResourceLocation(getItemName(ItemsRegistry.ROCKET_FIN.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistry.ROCKET_FAN.get())
                .define('X', ItemsRegistry.STEEL_INGOT.get()).define('#', ItemsRegistry.STEEL_NUGGET.get())
                .pattern(" # ")
                .pattern("#X#")
                .pattern(" # ")
                .unlockedBy(getHasName(ItemsRegistry.STEEL_NUGGET.get()), has(ItemsRegistry.STEEL_INGOT.get())).save(exporter, new ResourceLocation(getItemName(ItemsRegistry.ROCKET_FAN.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistry.ROCKET_ENGINE.get())
                .define('X', ItemsRegistry.STEEL_INGOT.get()).define('#', Items.IRON_INGOT).define('Y', ItemsRegistry.ROCKET_FAN.get()).define('R', Items.REDSTONE)
                .pattern("###")
                .pattern("R#X")
                .pattern("XYX")
                .unlockedBy(getHasName(Items.REDSTONE), has(ItemsRegistry.STEEL_INGOT.get())).save(exporter, new ResourceLocation(getItemName(ItemsRegistry.ROCKET_ENGINE.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistry.FUEL_ROCKET_MOTOR.get())
                .define('X', ItemsRegistry.STEEL_INGOT.get()).define('#', Items.IRON_INGOT).define('Y', ItemsRegistry.ROCKET_FAN.get()).define('R', ItemsRegistry.STEEL_NUGGET.get())
                .pattern("XX ")
                .pattern("Y#R")
                .pattern("XX ")
                .unlockedBy(getHasName(Items.REDSTONE), has(ItemsRegistry.STEEL_INGOT.get())).save(exporter, new ResourceLocation(getItemName(ItemsRegistry.FUEL_ROCKET_MOTOR.get())));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemsRegistry.FUEL_ROCKET_MOTOR.get())
                .define('X', ItemsRegistry.STEEL_INGOT.get()).define('#', Items.IRON_INGOT).define('Y', ItemsRegistry.ROCKET_FAN.get()).define('R', ItemsRegistry.STEEL_NUGGET.get())
                .pattern("XX ")
                .pattern("Y#R")
                .pattern("XX ")
                .unlockedBy(getHasName(Items.REDSTONE), has(ItemsRegistry.STEEL_INGOT.get())).save(exporter, new ResourceLocation(getItemName(ItemsRegistry.FUEL_ROCKET_MOTOR.get())));

    }

    public static void customNineBlockStorageRecipes(RecipeOutput recipeOutput, RecipeCategory recipeCategory, ItemLike itemLike, RecipeCategory recipeCategory2, ItemLike itemLike2) {
        nineBlockStorageRecipes(recipeOutput, recipeCategory, itemLike, recipeCategory2, itemLike2, getRecipeName(itemLike, itemLike2), (String)null, getRecipeName(itemLike2, itemLike), (String)null);
    }

    public static String getRecipeName(ItemLike itemLike1, ItemLike itemLike2) {
        return (getItemName(itemLike1) + "_to_" + getItemName(itemLike2));
    }


}

