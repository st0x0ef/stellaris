package com.st0x0ef.stellaris.neoforge.systems.resources.item.ingredient;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.msrandom.multiplatform.annotations.Actual;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;
import net.neoforged.neoforge.common.crafting.IntersectionIngredient;

public class ItemIngredientActual {
    @Actual
    public static Ingredient all(Ingredient... ingredients) {
        return IntersectionIngredient.of(ingredients);
    }

    @Actual
    public static Ingredient any(Ingredient... ingredients) {
        return CompoundIngredient.of(ingredients);
    }

    @Actual
    public static Ingredient difference(Ingredient base, Ingredient subtracted) {
        return DifferenceIngredient.of(base, subtracted);
    }

    @Actual
    public static Ingredient components(Ingredient base, DataComponentPredicate components) {
        ItemStack[] items = base.getItems();
        Holder<Item>[] holders = new Holder[items.length];
        for (int i = 0; i < items.length; i++) {
            holders[i] = items[i].getItem().builtInRegistryHolder();
        }
        HolderSet<Item> set = HolderSet.direct(holders);
        return new DataComponentIngredient(set, components, true).toVanilla();
    }

    @Actual
    public static Ingredient components(ItemStack stack) {
        return DataComponentIngredient.of(true, stack);
    }

    @Actual
    private static MapCodec<Ingredient> getNonEmptyMapCodec() {
        return Ingredient.MAP_CODEC_NONEMPTY;
    }
}
