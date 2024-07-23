package com.st0x0ef.stellaris.platform.systems.data;

import net.minecraft.core.component.DataComponentType;

import java.util.function.UnaryOperator;

public interface DataManager<T> {
    T get(Object dataHolder);

    T getOrThrow(Object dataHolder);

    T getOrCreate(Object dataHolder, T data);

    T set(Object dataHolder, T data);

    T remove(Object dataHolder);

    boolean has(Object dataHolder);

    DataComponentType<T> componentType();

    default T modify(Object dataHolder, UnaryOperator<T> modifier) {
        T data = get(dataHolder);
        T modifiedData = modifier.apply(data);
        set(dataHolder, modifiedData);
        return modifiedData;
    }
}
