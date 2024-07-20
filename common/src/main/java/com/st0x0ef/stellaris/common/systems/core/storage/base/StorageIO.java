package com.st0x0ef.stellaris.common.systems.core.storage.base;

public interface StorageIO<T> {
    long insert(T resource, long amount, boolean simulate);

    long extract(T resource, long amount, boolean simulate);
}