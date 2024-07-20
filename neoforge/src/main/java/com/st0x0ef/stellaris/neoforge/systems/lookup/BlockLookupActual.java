package com.st0x0ef.stellaris.neoforge.systems.lookup;

import com.st0x0ef.stellaris.common.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.neoforge.systems.lookup.impl.NeoBlockLookup;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface BlockLookupActual {
    @Actual
    static <T, C> BlockLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        NeoBlockLookup<T, C> lookup = new NeoBlockLookup<>(name, typeClass, contextClass);
        RegistryEventListener.REGISTRARS.add(lookup);
        return lookup;
    }
}
