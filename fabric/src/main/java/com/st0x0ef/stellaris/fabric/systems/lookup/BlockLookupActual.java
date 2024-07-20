package com.st0x0ef.stellaris.fabric.systems.lookup;

import com.st0x0ef.stellaris.common.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.fabric.systems.lookup.impl.FabricBlockLookup;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface BlockLookupActual {
    @Actual
    static <T, C> BlockLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricBlockLookup<>(name, typeClass, contextClass);
    }
}
