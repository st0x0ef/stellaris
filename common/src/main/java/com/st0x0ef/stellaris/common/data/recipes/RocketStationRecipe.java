package com.st0x0ef.stellaris.common.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class RocketStationRecipe implements Recipe<SimpleContainer> {

    private final ItemStack output;
    private final List<Ingredient> recipeItems;

    public RocketStationRecipe(List<Ingredient> recipeItems, ItemStack output) {
        this.recipeItems = recipeItems;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if(level.isClientSide()) {
            return false;
        }

        for (int i = 0; i < container.getContainerSize() - 1; i++) {
            if (!recipeItems.get(i).test(container.getItem(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
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
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<RocketStationRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "rocket_station";

    }

    public static class Serializer implements RecipeSerializer<RocketStationRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "rocket_station";

        public static final MapCodec<RocketStationRecipe> CODEC = RecordCodecBuilder.mapCodec(in -> in.group(
                validateAmount(Ingredient.CODEC_NONEMPTY, 14).fieldOf("ingredients").forGetter(RocketStationRecipe::getIngredients),
                ItemStack.CODEC.fieldOf("output").forGetter(r -> r.output)
        ).apply(in, RocketStationRecipe::new));

        private static Codec<List<Ingredient>> validateAmount(Codec<Ingredient> delegate, int max) {
            return delegate.listOf(1, max);
        }

        @Override
        public MapCodec<RocketStationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RocketStationRecipe> streamCodec() {
            return (StreamCodec<RegistryFriendlyByteBuf, RocketStationRecipe>) CODEC; // idk if that work
        }
    }
}
