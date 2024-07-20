package com.st0x0ef.stellaris.neoforge.systems.lookup.impl;

import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.neoforge.systems.lookup.RegistryEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NeoItemLookup<T, C> implements ItemLookup<T, C>, RegistryEventListener {
    private final List<Consumer<ItemRegistrar<T, C>>> registrars = new ArrayList<>();
    private final ItemCapability<T, C> capability;

    public NeoItemLookup(ItemCapability<T, C> capability) {
        this.capability = capability;
    }

    public NeoItemLookup(ResourceLocation id, Class<T> type, Class<C> contextType) {
        this(ItemCapability.create(id, type, contextType));
    }

    @Override
    public @Nullable T find(ItemStack stack, C context) {
        return stack.getCapability(capability, context);
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<T, C>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, items) -> event.registerItem(capability, getter::getContainer, items)));
    }
}
