package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.armors.JetSuit;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.io.Serializable;

public record RadioactiveComponent(int level, boolean isBlock) implements Serializable {

    public static final Codec<RadioactiveComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("level").forGetter(RadioactiveComponent::level),
            Codec.BOOL.fieldOf("is_block").forGetter(RadioactiveComponent::isBlock)
    ).apply(instance, RadioactiveComponent::new));

    public static final StreamCodec<ByteBuf, RadioactiveComponent> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.fromCodec(Codec.INT), RadioactiveComponent::level, ByteBufCodecs.fromCodec(Codec.BOOL), RadioactiveComponent::isBlock, RadioactiveComponent::new);
    }
}
