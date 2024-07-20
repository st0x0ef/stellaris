package com.st0x0ef.stellaris.fabric.systems.lookup;

import com.st0x0ef.stellaris.common.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.fabric.systems.lookup.impl.FabricEntityLookup;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface EntityLookupActual {
    @Actual
    static <T, C> EntityLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricEntityLookup<>(name, typeClass, contextClass);
    }
}
