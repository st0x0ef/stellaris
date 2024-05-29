package com.st0x0ef.stellaris.common.energy.generic.base;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface EntityContainerLookup<T, C> {
    @Nullable T find(Entity var1, C var2);

    void registerEntityTypes(EntityGetter<T, C> var1, Supplier<EntityType<?>>... var2);

    public interface EntityGetter<T, C> {
        T getContainer(Entity var1, C var2);
    }
}
