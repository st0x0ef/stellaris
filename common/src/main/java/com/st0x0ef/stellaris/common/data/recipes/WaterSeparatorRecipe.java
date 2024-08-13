package com.st0x0ef.stellaris.common.data.recipes;

import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public record WaterSeparatorRecipe(FluidStack ingredientStack, List<FluidStack> resultStacks, boolean isMb,
                                   long energy) implements Recipe<WaterSeparatorBlockEntity> {

    @Override
    public boolean matches(WaterSeparatorBlockEntity container, Level level) {
        FluidStack stack = container.getIngredientTank().getStack();
        return stack.isFluidEqual(ingredientStack) && stack.getAmount() >= ingredientStack.getAmount();
    }

    @Override
    public ItemStack assemble(WaterSeparatorBlockEntity container, RegistryAccess registryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        //return RecipesRegistry.WATER_SEPERATOR_SERIALIZER.get();
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipesRegistry.WATER_SEPERATOR_TYPE.get();
    }

    /*public static class Serializer implements RecipeSerializer<WaterSeparatorRecipe> {

        private static final Codec<WaterSeparatorRecipe> CODEC = RecordCodecBuilder.create(in -> in.group(
                FluidStack.CODEC.fieldOf("ingredient").forGetter(WaterSeparatorRecipe::ingredientStack),
                FluidStack.CODEC.listOf(1, 2).fieldOf("results").forGetter(WaterSeparatorRecipe::resultStacks),
                Codec.BOOL.optionalFieldOf("isFluidMB").forGetter(recipe -> Optional.of(recipe.isMb)),
                Codec.LONG.fieldOf("energy").forGetter(WaterSeparatorRecipe::energy)
        ).apply(in, WaterSeparatorRecipe::new));


        public static void convertFluidStack(FluidStack stack, boolean isMb) {
            stack.setAmount(isMb && Platform.isFabric() ? FluidTankHelper.convertFromMb(stack.getAmount()) : stack.getAmount());
        }

        @Override
        public Codec<WaterSeparatorRecipe> codec() {
            return CODEC;
        }

        @Override
        public WaterSeparatorRecipe fromNetwork(FriendlyByteBuf friendlyByteBuf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(friendlyByteBuf.readInt(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(friendlyByteBuf));
            }
            ItemStack output = friendlyByteBuf.readItem();
            return new WaterSeparatorRecipe(inputs, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, WaterSeparatorRecipe recipe) {
            friendlyByteBuf.writeInt(recipe.getIngredients().size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(friendlyByteBuf);
            }
            friendlyByteBuf.writeItem(recipe.getResultItem(null));
        }
    }*/
}
