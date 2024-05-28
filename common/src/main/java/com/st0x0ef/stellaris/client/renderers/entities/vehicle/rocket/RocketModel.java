package com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.animal.sniffer.Sniffer;

import java.util.function.IntFunction;

public enum RocketModel implements StringRepresentable {
    TINY(0),
    SMALL(1),
    NORMAL(2),
    BIG(3);

    public static final Codec<RocketModel> CODEC = StringRepresentable.fromEnum(RocketModel::values);


    public static final IntFunction<RocketModel> BY_ID = ByIdMap.continuous(RocketModel::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, RocketModel> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, RocketModel::id);
    private final int id;

    private RocketModel(final int id) {
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
            default -> "normal";
        };
    }


    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
