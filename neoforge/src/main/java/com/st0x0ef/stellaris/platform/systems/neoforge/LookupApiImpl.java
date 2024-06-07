package com.st0x0ef.stellaris.platform.systems.neoforge;

import com.st0x0ef.stellaris.common.systems.generic.base.BlockContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.EntityContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.ItemContainerLookup;
import com.st0x0ef.stellaris.neoforge.systems.generic.NeoForgeCapsHandler;
import net.minecraft.resources.ResourceLocation;

public class LookupApiImpl {

    public static <T, C> BlockContainerLookup<T, C> createBlockLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return NeoForgeCapsHandler.registerBlockLookup(name, typeClass, contextClass);
    }


    public static <T, C> ItemContainerLookup<T, C> createItemLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return NeoForgeCapsHandler.registerItemLookup(name, typeClass, contextClass);
    }


    public static <T, C> EntityContainerLookup<T, C> createEntityLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return NeoForgeCapsHandler.registerEntityLookup(name, typeClass, contextClass);
    }
}
