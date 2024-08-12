package com.st0x0ef.stellaris.common.data.recipes;

import com.st0x0ef.stellaris.common.blocks.entities.machines.RocketStationEntity;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class RocketStationRecipe implements Recipe<RocketStationEntity> {

    private final ItemStack output;
    private final List<Ingredient> recipeItems;
    public static RecipeType<RocketStationRecipe> Type = RecipesRegistry.ROCKET_STATION_TYPE.get();

    public RocketStationRecipe(List<Ingredient> recipeItems, ItemStack output) {
        this.recipeItems = recipeItems;
        this.output = output;
    }

    @Override
    public boolean matches(RocketStationEntity container, Level level) {
        for (int i = 0; i < container.getContainerSize() - 1; i++) {
            if (!recipeItems.get(i).test(container.getItem(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(RocketStationEntity container, RegistryAccess registryAccess) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.createWithCapacity(this.recipeItems.size());
        list.addAll(this.recipeItems);
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.ROCKET_STATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type;
    }
}
