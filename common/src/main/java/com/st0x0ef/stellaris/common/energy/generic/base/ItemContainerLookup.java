package com.st0x0ef.stellaris.common.energy.generic.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface ItemContainerLookup<T, C> {
    @Nullable T find(ItemStack var1, @Nullable C var2);

    void registerItems(ItemGetter<T, C> var1, Supplier<Item>... var2);

    public interface ItemGetter<T, C> {
        T getContainer(ItemStack var1, @Nullable C var2);
    }
}
