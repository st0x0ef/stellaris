package com.st0x0ef.stellaris.common.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.entities.machines.RocketStationEntity;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
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

    public static class Serializer implements RecipeSerializer<RocketStationRecipe> {
        public static final Codec<RocketStationRecipe> CODEC = RecordCodecBuilder.create(in -> in.group(
                validateAmount(Ingredient.CODEC_NONEMPTY, 14).fieldOf("ingredients").forGetter(RocketStationRecipe::getIngredients),
                ItemStack.RESULT_CODEC.fieldOf("output").forGetter(r -> r.output)
        ).apply(in, RocketStationRecipe::new));

        private static Codec<List<Ingredient>> validateAmount(Codec<Ingredient> delegate, int max) {
            return ExtraCodecs.validate(ExtraCodecs.validate(
                    delegate.listOf(), list -> list.size() > max ? DataResult.error(() -> "Recipe has too many ingredients!") : DataResult.success(list)
            ), list -> list.isEmpty() ? DataResult.error(() -> "Recipe has no ingredients!") : DataResult.success(list));
        }

        @Override
        public Codec<RocketStationRecipe> codec() {
            return CODEC;
        }

        @Override
        public RocketStationRecipe fromNetwork(FriendlyByteBuf friendlyByteBuf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(friendlyByteBuf.readInt(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(friendlyByteBuf));
            }
            ItemStack output = friendlyByteBuf.readItem();
            return new RocketStationRecipe(inputs, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, RocketStationRecipe recipe) {
            friendlyByteBuf.writeInt(recipe.recipeItems.size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(friendlyByteBuf);
            }
            friendlyByteBuf.writeItem(recipe.getResultItem(null));
        }
    }
}