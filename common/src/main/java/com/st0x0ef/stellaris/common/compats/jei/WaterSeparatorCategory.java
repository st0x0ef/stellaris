package com.st0x0ef.stellaris.common.compats.jei;

import com.st0x0ef.stellaris.common.data.recipes.WaterSeparatorRecipe;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import dev.architectury.fluid.FluidStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import static com.st0x0ef.stellaris.Stellaris.guiTexture;
import static com.st0x0ef.stellaris.Stellaris.id;

public record WaterSeparatorCategory(IGuiHelper guiHelper) implements IRecipeCategory<WaterSeparatorRecipe> {

    public static final ResourceLocation TEXTURE = guiTexture("compats/water_separator");
    public static final RecipeType<WaterSeparatorRecipe> RECIPE = new RecipeType<>(id("water_separator"), WaterSeparatorRecipe.class);

    @Override
    public RecipeType<WaterSeparatorRecipe> getRecipeType() {
        return RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("stellaris.compat.water_separator");
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createBlankDrawable(256, 256);
    }

    @Override
    public IDrawable getIcon() {
        return guiHelper.createDrawableItemStack(ItemsRegistry.WATER_SEPARATOR.get().getDefaultInstance());
    }

    @Override
    public void draw(WaterSeparatorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        graphics.blit(TEXTURE, 0, 0, 0, 0, 256, 256, 256, 256);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WaterSeparatorRecipe recipe, IFocusGroup focuses) {
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST)
                .addIngredients(Ingredient.of(ItemsRegistry.WATER_SEPARATOR.get()));
        List<FluidStack> results = recipe.resultStacks();
        if (!results.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 19, 113).addFluidStack(results.get(1).getFluid(), results.get(1).getAmount());
        }
        if (results.size() > 1) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 139, 113).addFluidStack(results.get(1).getFluid(), results.get(1).getAmount());
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 113).addFluidStack(recipe.ingredientStack().getFluid(), recipe.ingredientStack().getAmount());
        builder.addSlot(RecipeIngredientRole.INPUT, 103, 113).addFluidStack(recipe.ingredientStack().getFluid(), recipe.ingredientStack().getAmount());
    }
}

   /* private static void inputSlotAdder(IRecipeLayoutBuilder builder, WaterSeparatorRecipe recipe, int x, int y, int index) {
        IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, x, y);
        if (index < recipe.ingredientStack().size()) {
            slot.addFluidStack(recipe.getIngredients().get(index));
        }
    }

    private static void outputSlotAdder(IRecipeLayoutBuilder builder, WaterSeparatorRecipe recipe, int x, int y, int index) {
        IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.OUTPUT, x, y);
        List<ItemStack> outputs = recipe.getResultItem
        if (index < outputs.size()) {
            slot.addItemStack(outputs.get(index));
        }
    }
}*/