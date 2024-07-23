package com.st0x0ef.stellaris.platform.systems.resources;

import com.st0x0ef.stellaris.platform.systems.resources.entity.ingredient.EntityIngredientRegistry;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.ingredient.FluidIngredientRegistry;

public class ResourceLib {
    public static final String MOD_ID = "stellaris_resources";

    public static void init() {
        FluidIngredientRegistry.init();
        EntityIngredientRegistry.init();
    }
}
