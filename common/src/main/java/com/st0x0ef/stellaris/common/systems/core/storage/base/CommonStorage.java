package com.st0x0ef.stellaris.common.systems.core.storage.base;

import com.st0x0ef.stellaris.common.systems.resources.Resource;
import com.st0x0ef.stellaris.common.systems.resources.ResourceStack;
import org.jetbrains.annotations.NotNull;

public interface CommonStorage<T extends Resource> extends StorageIO<T> {
    int size();

    @NotNull
    StorageSlot<T> get(int index);

    default boolean allowsInsertion() {
        return true;
    }

    default boolean allowsExtraction() {
        return true;
    }

    default long getLimit(int index, T resource) {
        return get(index).getLimit(resource);
    }

    default T getResource(int index) {
        return get(index).getResource();
    }

    default ResourceStack<T> getContents(int index) {
        return get(index).getContents();
    }

    default boolean isResourceValid(int index, T resource) {
        return get(index).isResourceValid(resource);
    }

    default long getAmount(int index) {
        return get(index).getAmount();
    }

    default long insert(int index, T resource, long amount, boolean simulate) {
        return get(index).insert(resource, amount, simulate);
    }

    default long extract(int index, T resource, long amount, boolean simulate) {
        return get(index).extract(resource, amount, simulate);
    }

    @SuppressWarnings("unchecked")
    static <T extends Resource> Class<CommonStorage<T>> asClass() {
        return (Class<CommonStorage<T>>) (Object) CommonStorage.class;
    }
}
