package com.st0x0ef.stellaris.platform.systems.lookup.impl;

import com.st0x0ef.stellaris.platform.systems.lookup.ItemLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FabricItemLookup<T, C> implements ItemLookup<T, C> {
    private final ItemApiLookup<T, C> lookup;

    public FabricItemLookup(ItemApiLookup<T, C> lookup) {
        this.lookup = lookup;
    }

    public FabricItemLookup(ResourceLocation id, Class<T> type, Class<C> contextType) {
        this(ItemApiLookup.get(id, type, contextType));
    }

    @Override
    public @Nullable T find(ItemStack stack, C context) {
        return lookup.find(stack, context);
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<T, C>> registrar) {
        registrar.accept((getter, containers) -> lookup.registerForItems(getter::getContainer, containers));
    }
}
