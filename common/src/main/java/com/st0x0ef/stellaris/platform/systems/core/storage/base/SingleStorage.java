package com.st0x0ef.stellaris.platform.systems.core.storage.base;

import com.st0x0ef.stellaris.platform.systems.resources.Resource;
import org.jetbrains.annotations.NotNull;

public interface SingleStorage<T extends Resource> extends CommonStorage<T>, StorageSlot<T> {
    default int size() {
        return 1;
    }

    @Override
    default long getAmount(int index) {
        return getAmount();
    }

    @Override
    default T getResource(int index) {
        return getResource();
    }

    @Override
    default boolean isResourceValid(int index, T resource) {
        return isResourceValid(resource);
    }

    @Override
    default long getLimit(int index, T resource) {
        return getLimit(resource);
    }

    @Override
    default long extract(int index, T resource, long amount, boolean simulate) {
        return extract(resource, amount, simulate);
    }

    @Override
    default long insert(int index, T resource, long amount, boolean simulate) {
        return insert(resource, amount, simulate);
    }

    default @NotNull StorageSlot<T> get(int index) {
        return this;
    }
}
