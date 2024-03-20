package com.st0x0ef.stellaris.common.config;

import net.minecraft.util.StringRepresentable;

import java.io.Serializable;

public abstract class ConfigEntry implements Serializable {

    public abstract ConfigType getType();

    private static String name;
    private static String description;

    public ConfigEntry(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static class ConfigEntryString extends ConfigEntry {
        private final String value;
        private final String description;

        public ConfigEntryString(String name, String description) {
            super(name, description);
            this.value = name;
            this.description = description;
        }

        @Override
        public ConfigType getType() {
            return ConfigType.INT;
        }

    }

    public enum ConfigType implements StringRepresentable {
        STRING,
        INT;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }


}
