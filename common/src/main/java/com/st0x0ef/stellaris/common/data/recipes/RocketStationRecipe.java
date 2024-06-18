package com.st0x0ef.stellaris.common.data.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.entities.machines.RocketStationEntity;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
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
    public ItemStack assemble(RocketStationEntity container, HolderLookup.Provider provider) {
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
        return RecipesRegistry.ROCKET_STATION.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type;
    }

    public static class Serializer implements RecipeSerializer<RocketStationRecipe> {

        public static final MapCodec<RocketStationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.listOf(1, 14).fieldOf("ingredients").forGetter(r -> r.recipeItems),
                ItemStack.CODEC.fieldOf("output").forGetter(r -> r.output)
        ).apply(instance, RocketStationRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, List<Ingredient>> INGREDIENT_LIST_STREAM_CODEC = ByteBufCodecs.collection(ArrayList::new, Ingredient.CONTENTS_STREAM_CODEC, 14);
        public static final StreamCodec<RegistryFriendlyByteBuf, RocketStationRecipe> STREAM_CODEC = StreamCodec.of((buf, recipe) -> {
            INGREDIENT_LIST_STREAM_CODEC.encode(buf, recipe.recipeItems);
            ItemStack.STREAM_CODEC.encode(buf, recipe.output);
        }, buf -> new RocketStationRecipe(INGREDIENT_LIST_STREAM_CODEC.decode(buf), ItemStack.STREAM_CODEC.decode(buf)));

        @Override
        public MapCodec<RocketStationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RocketStationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
