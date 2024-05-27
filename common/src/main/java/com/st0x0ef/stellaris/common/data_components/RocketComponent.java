package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record RocketComponent(String skin, int fuel) {

    public static final Codec<RocketComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("skin").forGetter(RocketComponent::skin),
            Codec.INT.fieldOf("fuel").forGetter(RocketComponent::fuel)
    ).apply(instance, RocketComponent::new));

    public static final StreamCodec<ByteBuf, RocketComponent> STREAM_CODEC;


    public RocketComponent(String skin, int fuel) {

        this.skin = skin;
        this.fuel = fuel;
    }

    public int getFuel() {
        return fuel;
    }

    public ResourceLocation getSkin() {
        return new ResourceLocation(skin);
    }

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, RocketComponent::skin, ByteBufCodecs.INT, RocketComponent::fuel, RocketComponent::new);
    }
}
