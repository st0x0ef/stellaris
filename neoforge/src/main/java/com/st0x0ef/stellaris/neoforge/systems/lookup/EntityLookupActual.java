package com.st0x0ef.stellaris.neoforge.systems.lookup;

import com.st0x0ef.stellaris.common.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.neoforge.systems.lookup.impl.NeoEntityLookup;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface EntityLookupActual {
    @Actual
    static <T, C> EntityLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        NeoEntityLookup<T, C> lookup = new NeoEntityLookup<>(name, typeClass, contextClass);
        RegistryEventListener.REGISTRARS.add(lookup);
        return lookup;
    }
}
