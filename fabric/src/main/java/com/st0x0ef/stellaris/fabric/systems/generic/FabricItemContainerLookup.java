package com.st0x0ef.stellaris.fabric.systems.generic;

import com.st0x0ef.stellaris.common.systems.generic.base.ItemContainerLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.impl.lookup.item.ItemApiLookupImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class FabricItemContainerLookup<T, C> implements ItemContainerLookup<T, C> {
    private final ItemApiLookup<T, C> lookupMap;

    public FabricItemContainerLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        lookupMap = ItemApiLookupImpl.get(name, typeClass, contextClass);
    }

    @Override
    public T find(ItemStack stack, C context) {
        return lookupMap.find(stack, null);
    }

    @Override
    public void registerItems(ItemGetter<T, C> getter, Supplier<Item>... containers) {
        for (Supplier<Item> container : containers) {
            lookupMap.registerForItems(getter::getContainer, container.get());
        }
    }

    public ItemApiLookup<T, C> getLookupMap() {
        return lookupMap;
    }
}
