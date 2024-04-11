package com.st0x0ef.stellaris.common.config;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ConfigEntry<T> implements Serializable {
    private T value;
    private String description;

    public ConfigEntry(T value, String description) {
        this.value = value;
        this.description = description;
    }

    public T getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return this.getValue().getClass();
    }
}