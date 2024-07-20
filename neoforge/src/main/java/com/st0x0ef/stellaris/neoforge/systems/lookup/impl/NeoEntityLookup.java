package com.st0x0ef.stellaris.neoforge.systems.lookup.impl;

import com.st0x0ef.stellaris.common.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.neoforge.systems.lookup.RegistryEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NeoEntityLookup<T, C> implements EntityLookup<T, C>, RegistryEventListener {
    private final List<Consumer<EntityRegistrar<T, C>>> registrars = new ArrayList<>();
    private final EntityCapability<T, C> capability;

    public NeoEntityLookup(EntityCapability<T, C> capability) {
        this.capability = capability;
    }

    public NeoEntityLookup(ResourceLocation id, Class<T> type, Class<C> contextType) {
        this(EntityCapability.create(id, type, contextType));
    }

    @Override
    public @Nullable T find(Entity entity, C context) {
        return entity.getCapability(capability, context);
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<T, C>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, types) -> {
            for (EntityType<?> type : types) {
                event.registerEntity(capability, type, getter::getContainer);
            }
        }));
    }
}
