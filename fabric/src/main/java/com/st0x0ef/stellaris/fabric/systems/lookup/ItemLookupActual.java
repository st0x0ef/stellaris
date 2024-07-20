package com.st0x0ef.stellaris.fabric.systems.lookup;

import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.fabric.systems.lookup.impl.FabricItemLookup;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface ItemLookupActual {
    @Actual
    static <T, C> ItemLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricItemLookup<>(name, typeClass, contextClass);
    }
}
