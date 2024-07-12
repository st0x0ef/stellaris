package com.st0x0ef.stellaris.common.config;

import java.io.Serializable;
import java.lang.reflect.Type;

public record ConfigEntry<T>(T value, String description) implements Serializable {
    public Type getType() {
        return this.value().getClass();
    }
}