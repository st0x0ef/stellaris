package com.st0x0ef.stellaris.platform.systems.fabric;

import com.st0x0ef.stellaris.common.systems.generic.base.BlockContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.EntityContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.ItemContainerLookup;
import com.st0x0ef.stellaris.fabric.systems.generic.FabricBlockContainerLookup;
import com.st0x0ef.stellaris.fabric.systems.generic.FabricEntityContainerLookup;
import com.st0x0ef.stellaris.fabric.systems.generic.FabricItemContainerLookup;
import net.minecraft.resources.ResourceLocation;

public class LookupApiImpl {

    public static <T, C> BlockContainerLookup<T, C> createBlockLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricBlockContainerLookup<>(name, typeClass, contextClass);
    }

    public static <T, C> ItemContainerLookup<T, C> createItemLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricItemContainerLookup<>(name, typeClass, contextClass);
    }

    public static <T, C> EntityContainerLookup<T, C> createEntityLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricEntityContainerLookup<>(name, typeClass, contextClass);
    }
}
