package com.st0x0ef.stellaris.common.compats.jei;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.recipes.WaterSeparatorRecipe;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
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
import org.jetbrains.annotations.Nullable;

public record WaterSeparatorCategory (IGuiHelper guiHelper) implements IRecipeCategory<WaterSeparatorRecipe> {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/compats/water_separator.png");
    public static final RecipeType<WaterSeparatorRecipe> RECIPE = new RecipeType<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "water_separator"), WaterSeparatorRecipe.class);
    @Override
    public RecipeType<WaterSeparatorRecipe> getRecipeType() {
        return RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("stellaris.water_separator_crafting");
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createBlankDrawable(177, 224);
    }
    @Override
    public void draw(WaterSeparatorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        graphics.blit(TEXTURE, 0, 0, 0, 0, 256, 256, 256, 256);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return guiHelper.createDrawableItemStack(ItemsRegistry.WATER_SEPARATOR.get().getDefaultInstance());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WaterSeparatorRecipe recipe, IFocusGroup focuses) {
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST).addIngredients(Ingredient.of(ItemsRegistry.WATER_SEPARATOR.get()));
        inputSlotAdder(builder, recipe, 56, 20, 0);
        inputSlotAdder(builder, recipe,47, 38, 1);
        inputSlotAdder(builder, recipe,65, 38, 2);
        inputSlotAdder(builder, recipe,47, 56, 3);
        inputSlotAdder(builder, recipe,65, 56, 4);
        inputSlotAdder(builder, recipe,47, 74, 5);
        inputSlotAdder(builder, recipe,65, 74, 6);
        inputSlotAdder(builder, recipe,29, 92, 7);
        inputSlotAdder(builder, recipe,47, 92, 8);
        inputSlotAdder(builder, recipe,65, 92, 9);
        inputSlotAdder(builder, recipe,83, 92, 10);
        inputSlotAdder(builder, recipe,29, 110, 11);
        inputSlotAdder(builder, recipe,56, 110, 12);
        inputSlotAdder(builder, recipe,83, 110, 13);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 129, 56).addItemStack(recipe.getResultItem(null));


    }
    public static void inputSlotAdder(IRecipeLayoutBuilder builder, WaterSeparatorRecipe recipe, int x, int y, int index){
        IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, x, y);
        if(index < recipe.getIngredients().size()){
            slot.addIngredients(recipe.getIngredients().get(index));
        }
    }
}
