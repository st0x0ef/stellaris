package com.st0x0ef.stellaris.fabric.systems.generic;

import com.st0x0ef.stellaris.common.systems.generic.base.EntityContainerLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.impl.lookup.entity.EntityApiLookupImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class FabricEntityContainerLookup<T, C> implements EntityContainerLookup<T, C> {
    private final EntityApiLookup<T, C> lookupMap;

    public FabricEntityContainerLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        lookupMap = EntityApiLookupImpl.get(name, typeClass, contextClass);
    }

    @Override
    public T find(Entity stack, C context) {
        return lookupMap.find(stack, context);
    }

    @Override
    public void registerEntityTypes(EntityGetter<T, C> getter, Supplier<EntityType<?>>... containers) {
        for (Supplier<EntityType<?>> container : containers) {
            lookupMap.registerForTypes(getter::getContainer, container.get());
        }
    }

    public EntityApiLookup<T, C> getLookupMap() {
        return lookupMap;
    }
}
