package com.st0x0ef.stellaris.common.systems.resources.entity.ingredient.impl;


import com.st0x0ef.stellaris.common.systems.resources.entity.ingredient.EntityIngredient;

import java.util.List;

public interface ListEntityIngredient extends EntityIngredient {
    List<EntityIngredient> children();

    @Override
    default boolean requiresTesting() {
        for (EntityIngredient ingredient : children()) {
            if (ingredient.requiresTesting()) {
                return true;
            }
        }
        return false;
    }
}
