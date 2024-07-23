package com.st0x0ef.stellaris.platform.systems.resources.fluid.ingredient.impl;


import com.st0x0ef.stellaris.platform.systems.resources.fluid.ingredient.FluidIngredient;

import java.util.List;

public interface ListFluidIngredient extends FluidIngredient {
    List<FluidIngredient> children();

    @Override
    default boolean requiresTesting() {
        for (FluidIngredient ingredient : children()) {
            if (ingredient.requiresTesting()) {
                return true;
            }
        }
        return false;
    }
}
