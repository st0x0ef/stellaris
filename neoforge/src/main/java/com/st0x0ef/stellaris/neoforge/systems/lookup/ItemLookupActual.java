package com.st0x0ef.stellaris.neoforge.systems.lookup;

import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.neoforge.systems.lookup.impl.NeoItemLookup;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface ItemLookupActual {
    @Actual
    static <T, C> ItemLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        NeoItemLookup<T, C> lookup = new NeoItemLookup<>(name, typeClass, contextClass);
        RegistryEventListener.REGISTRARS.add(lookup);
        return lookup;
    }
}
