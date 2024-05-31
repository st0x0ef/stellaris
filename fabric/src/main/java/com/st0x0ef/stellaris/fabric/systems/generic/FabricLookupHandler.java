package com.st0x0ef.stellaris.fabric.systems.generic;

import com.st0x0ef.stellaris.common.systems.generic.base.BlockContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.EntityContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.ItemContainerLookup;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;

public class FabricLookupHandler {
    public static <T, C> BlockApiLookup<T, C> getAsLookup(BlockContainerLookup<T, C> lookup) {
        if (lookup instanceof FabricBlockContainerLookup) {
            return ((FabricBlockContainerLookup<T, C>) lookup).getLookupMap();
        }
        return null;
    }

    public static <T, C> ItemApiLookup<T, C> getAsLookup(ItemContainerLookup<T, C> lookup) {
        if (lookup instanceof FabricItemContainerLookup) {
            return ((FabricItemContainerLookup<T, C>) lookup).getLookupMap();
        }
        return null;
    }

    public static <T, C> EntityApiLookup<T, C> getAsLookup(EntityContainerLookup<T, C> lookup) {
        if (lookup instanceof FabricEntityContainerLookup) {
            return ((FabricEntityContainerLookup<T, C>) lookup).getLookupMap();
        }
        return null;
    }
}
