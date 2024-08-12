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
        return RecipesRegistry.WATER_SEPERATOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipesRegistry.WATER_SEPERATOR_TYPE.get();
    }
}
