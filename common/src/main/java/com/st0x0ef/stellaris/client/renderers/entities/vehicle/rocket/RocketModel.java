package com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum RocketModel implements StringRepresentable {
    TINY(0),
    SMALL(1),
    NORMAL(2),
    BIG(3);

    public static final Codec<RocketModel> CODEC = StringRepresentable.fromEnum(RocketModel::values);
    private final int id;

    RocketModel(final int id) {
        this.id = id;
    }

    public int id() {
        return this.id;
    }

    @Override
    public String toString() {
        return switch (this) {
            case TINY -> "tiny";
            case SMALL -> "small";
            case NORMAL -> "normal";
            case BIG -> "big";
        };
    }

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static RocketModel fromString(String name) {
        return switch (name) {
            case "tiny" -> TINY;
            case "small" -> SMALL;
            case "big" -> BIG;
            default -> NORMAL;
        };
    }

    public double getPlayerYPos() {
        return switch (this) {
            case TINY -> 3.15d;
            case SMALL -> 2.0d;
            case NORMAL -> 3.15d;
            case BIG -> 2.5d;
        };
    }

    public int getMaxPlayerNumber() {
        return switch (this) {
            case TINY -> 1;
            case SMALL -> 2;
            case NORMAL -> 3;
            case BIG -> 4;
        };
    }
}
