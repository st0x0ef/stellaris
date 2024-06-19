package com.st0x0ef.stellaris.common.compat;


import com.st0x0ef.stellaris.common.data.recipes.RocketStationRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RocketStationDisplay extends BasicDisplay {

    public RocketStationDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public RocketStationDisplay(RecipeHolder<RocketStationRecipe> recipe) {
        super(getInputList(recipe.value()), List.of(EntryIngredient.of(EntryStacks.of(recipe.value().getResultItem(null)))));
    }

    private static List<EntryIngredient> getInputList(RocketStationRecipe recipe){
        if (recipe==null) return Collections.emptyList();
        List<EntryIngredient> list = new ArrayList<>();
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(0)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(1)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(2)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(3)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(4)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(5)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(6)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(7)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(8)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(9)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(10)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(11)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(12)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(13)));

        return list;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return RocketStationCategory.ROCKET_CRAFTING;
    }
}
