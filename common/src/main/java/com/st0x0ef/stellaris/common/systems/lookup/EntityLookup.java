package com.st0x0ef.stellaris.common.systems.lookup;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface EntityLookup<T, C> {

    @Expect
    static <T, C> EntityLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass);

    static <T> EntityLookup<T, Void> create(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, Void.class);
    }

    static <T> EntityLookup<T, Direction> createAutomation(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, Direction.class);
    }

    /**
     * @return The {@link T} for the block.
     */
    @Nullable
    T find(Entity entity, C context);

    default boolean isPresent(Entity entity, C context) {
        return find(entity, context) != null;
    }

    default void registerFallback(EntityGetter<T, C> getter) {
        onRegister(registrar -> {
            for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
                registrar.register(getter, entityType);
            }
        });
    }

    default void registerFallback(EntityGetter<T, C> getter, Predicate<EntityType<?>> predicate) {
        onRegister(registrar -> {
            for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
                if (predicate.test(entityType)) {
                    registrar.register(getter, entityType);
                }
            }
        });
    }

    void onRegister(Consumer<EntityRegistrar<T, C>> registrar);

    interface EntityRegistrar<T, C> {
        void register(EntityGetter<T, C> getter, EntityType<?>... entityTypes);
    }

    interface EntityGetter<T, C> {
        T getContainer(Entity entity, C context);
    }
}
