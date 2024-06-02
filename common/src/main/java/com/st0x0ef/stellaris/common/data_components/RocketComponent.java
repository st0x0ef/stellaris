package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import com.st0x0ef.stellaris.common.items.upgrade.FuelType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record RocketComponent(String skin, RocketModel model, FuelType.Type fuelType, int fuel, int tankCapacity) {

    public static final Codec<RocketComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("skin").forGetter(RocketComponent::skin),
            RocketModel.CODEC.fieldOf("model").forGetter(RocketComponent::model),
            FuelType.CODEC.fieldOf("fuel_type").forGetter(RocketComponent::fuelType),
            Codec.INT.fieldOf("fuel").forGetter(RocketComponent::fuel),
            Codec.INT.fieldOf("fuel_capacity").forGetter(RocketComponent::tankCapacity)
    ).apply(instance, RocketComponent::new));

    public static final StreamCodec<ByteBuf, RocketComponent> STREAM_CODEC;


    public int getFuel() {
        return fuel;
    }

    public ResourceLocation getSkin() {
        return new ResourceLocation(skin);
    }

    public RocketModel getModel() {
        return model;
    }

    public FuelType.Type getFuelType() {
        return fuelType;
    }

    public int getTankCapacity() {
        return tankCapacity;
    }

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, RocketComponent::skin, ByteBufCodecs.fromCodec(RocketModel.CODEC), RocketComponent::model, ByteBufCodecs.fromCodec(FuelType.CODEC), RocketComponent::fuelType, ByteBufCodecs.INT, RocketComponent::fuel, ByteBufCodecs.INT, RocketComponent::tankCapacity, RocketComponent::new);
    }
}
