package com.st0x0ef.stellaris.common.compats.jei;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.recipes.FuelRefineryRecipe;
import com.st0x0ef.stellaris.common.data.recipes.RocketStationRecipe;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public record FuelRefineryCategory(IGuiHelper guiHelper) implements IRecipeCategory<FuelRefineryRecipe> {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/compats/fuel_refinery.png");
    public static final RecipeType<FuelRefineryRecipe> RECIPE = new RecipeType<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "fuel_refinery"), FuelRefineryRecipe.class);

    @Override
    public RecipeType<FuelRefineryRecipe> getRecipeType() {
        return RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("stellaris.compat.rocket_crafting");
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createBlankDrawable(177, 132);
    }

    @Override
    public void draw(FuelRefineryRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        graphics.blit(TEXTURE, 0, 0, 0, 0, 256, 256, 256, 256);
    }

    @Override
    public IDrawable getIcon() {
        return guiHelper.createDrawableItemStack(ItemsRegistry.FUEL_REFINERY.get().getDefaultInstance());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FuelRefineryRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 14, 18);
    }
}
