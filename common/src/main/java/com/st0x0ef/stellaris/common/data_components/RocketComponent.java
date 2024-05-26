package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.PlanetTextures;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;

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
