package com.st0x0ef.stellaris.common.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record WaterSeparatorRecipe(FluidStack ingredientStack, List<FluidStack> resultStacks, int energy) implements Recipe<WaterSeparatorBlockEntity> {

    @Override
    public boolean matches(WaterSeparatorBlockEntity container, Level level) {
        FluidStack stack = container.getIngredientTank().getStack();
        return stack.isFluidEqual(ingredientStack) && stack.getAmount() >= ingredientStack.getAmount();
    }

    @Override
    public ItemStack assemble(WaterSeparatorBlockEntity container, HolderLookup.Provider registries) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.WATER_SEPERATOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipesRegistry.WATER_SEPERATOR_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<WaterSeparatorRecipe> {

        private static final MapCodec<WaterSeparatorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                FluidStack.CODEC.fieldOf("ingredient").forGetter(WaterSeparatorRecipe::ingredientStack),
                FluidStack.CODEC.listOf(1, 2).fieldOf("results").forGetter(WaterSeparatorRecipe::resultStacks),
                Codec.INT.fieldOf("energy").forGetter(WaterSeparatorRecipe::energy)
        ).apply(instance, WaterSeparatorRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, List<FluidStack>> FLUID_STACK_LIST_STREAM_CODEC =
                ByteBufCodecs.collection(ArrayList::new, FluidStack.STREAM_CODEC, 2);
        private static final StreamCodec<RegistryFriendlyByteBuf, WaterSeparatorRecipe> STREAM_CODEC = StreamCodec.of((buf, recipe) -> {
            recipe.ingredientStack().write(buf);
            FLUID_STACK_LIST_STREAM_CODEC.encode(buf, recipe.resultStacks);
            buf.writeInt(recipe.energy);
        }, buf -> new WaterSeparatorRecipe(FluidStack.read(buf), FLUID_STACK_LIST_STREAM_CODEC.decode(buf), buf.readInt()));

        @Override
        public MapCodec<WaterSeparatorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WaterSeparatorRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}