package com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

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
    public String getSerializedName() {
        return name().toLowerCase();
    }

    public static RocketModel fromString(String name) {
        return switch (name) {
            case "tiny" -> TINY;
            case "small" -> SMALL;
            case "normal" -> NORMAL;
            case "big" -> BIG;
            default -> NORMAL;
        };

    }
}
