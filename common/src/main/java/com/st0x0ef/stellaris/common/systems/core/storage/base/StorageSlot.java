package com.st0x0ef.stellaris.common.systems.core.storage.base;

import com.st0x0ef.stellaris.common.systems.resources.Resource;
import com.st0x0ef.stellaris.common.systems.resources.ResourceStack;

public interface StorageSlot<T extends Resource> extends StorageIO<T> {
    long getLimit(T resource);

    T getResource();

    default ResourceStack<T> getContents() {
        return new ResourceStack<>(getResource(), getAmount());
    }

    boolean isResourceValid(T resource);

    long getAmount();
}
