package com.st0x0ef.stellaris.common.config;

import net.minecraft.util.StringRepresentable;

import java.io.Serializable;

public abstract class ConfigEntry implements Serializable {

    public abstract ConfigType getType();

    private static Boolean boolName;
    private static int intName;
    private static String stringName;
    private static String description;

    public ConfigEntry(String name, String description) {
        this.stringName = name;
        this.description = description;
    }
    public ConfigEntry(int name, String description) {
        this.intName = name;
        this.description = description;
    }
    public ConfigEntry(Boolean name, String description) {
        this.boolName = name;
        this.description = description;
    }

    public String getName() {
        return stringName;
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

        //This was ConfigType.INT before changed it to STRING
        @Override
        public ConfigType getType() {
            return ConfigType.STRING;
        }

    }

    public static class ConfigEntryInt extends ConfigEntry {
        private final int value;
        private final String description;

        public ConfigEntryInt(int name, String description) {
            super(name, description);
            this.value = name;
            this.description = description;
        }

        @Override
        public ConfigType getType() {
            return ConfigType.INT;
        }

    }
    public static class ConfigEntryBool extends ConfigEntry {
        private final boolean value;
        private final String description;

        public ConfigEntryBool(boolean name, String description) {
            super(name, description);
            this.value = name;
            this.description = description;
        }

        @Override
        public ConfigType getType() {
            return ConfigType.BOOLEAN;
        }

    }

    public enum ConfigType implements StringRepresentable {
        STRING,
        BOOLEAN,
        INT;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }


}
