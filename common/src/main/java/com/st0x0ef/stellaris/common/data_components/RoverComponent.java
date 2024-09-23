package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import com.st0x0ef.stellaris.common.rocket_upgrade.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.io.Serializable;

public record RoverComponent(String fuelType, int fuel, ResourceLocation fuelTexture, int tankCapacity) implements Serializable {

    public static final Codec<RoverComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("fuel_type").forGetter(RoverComponent::fuelType),
            Codec.INT.fieldOf("fuel").forGetter(RoverComponent::fuel),
            ResourceLocation.CODEC.fieldOf("fuel_texture").forGetter(RoverComponent::fuelTexture),
            Codec.INT.fieldOf("fuel_capacity").forGetter(RoverComponent::tankCapacity)
    ).apply(instance, RoverComponent::new));

    public static final StreamCodec<ByteBuf, RoverComponent> STREAM_CODEC;

    public int getFuel() {
        return fuel;
    }

    public ResourceLocation getFuelTexture() {
        return fuelTexture;
    }



    public MotorUpgrade getMotorUpgrade() {
        return new MotorUpgrade(FuelType.Type.fromString(fuelType), fuelTexture);
    }

    public TankUpgrade getTankUpgrade() {
        return new TankUpgrade(tankCapacity);
    }


    public int getTankCapacity() {
        return tankCapacity;
    }

    static {
        STREAM_CODEC = StreamCodec.composite( ByteBufCodecs.STRING_UTF8, RoverComponent::fuelType, ByteBufCodecs.INT, RoverComponent::fuel, ResourceLocation.STREAM_CODEC, RoverComponent::fuelTexture, ByteBufCodecs.INT, RoverComponent::tankCapacity, RoverComponent::new);
    }
}
