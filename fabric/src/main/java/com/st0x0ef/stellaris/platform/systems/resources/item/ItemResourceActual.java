package com.st0x0ef.stellaris.platform.systems.resources.item;

import net.msrandom.multiplatform.annotations.Actual;

public class ItemResourceActual {
    @Actual
    private static ItemResource getCraftingRemainder(ItemResource resource) {
        return ItemResource.of(resource.getCachedStack().getRecipeRemainder());
    }

    @Actual
    private static boolean hasCraftingRemainder(ItemResource resource) {
        return resource.getCachedStack().getRecipeRemainder().isEmpty();
    }
}
