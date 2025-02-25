package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.vehicle_upgrade.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.io.Serializable;
import java.util.Optional;

public record RoverComponent(String fuelType, int fuel, ResourceLocation fuelTexture, int tankCapacity, float speedModifier) implements Serializable {

    public static final Codec<RoverComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("fuel_type").forGetter(RoverComponent::fuelType),
            Codec.INT.fieldOf("fuel").forGetter(RoverComponent::fuel),
            ResourceLocation.CODEC.fieldOf("fuel_texture").forGetter(RoverComponent::fuelTexture),
            Codec.INT.fieldOf("fuel_capacity").forGetter(RoverComponent::tankCapacity),
            Codec.FLOAT.fieldOf("speed_modifier").forGetter(RoverComponent::speedModifier)
    ).apply(instance, RoverComponent::new));

    public static final StreamCodec<ByteBuf, RoverComponent> STREAM_CODEC;

    public int getFuel() {
        return fuel;
    }

    @Deprecated
    public ResourceLocation getFuelTexture() {
        return this.getFuelType().getFuelTexture();
    }

    public MotorUpgrade getMotorUpgrade() {
        return new MotorUpgrade(this.getFuelType().getMotorType());
    }

    public FuelType.Type getFuelType() {
        FuelType.Type type = FuelType.Type.fromString(fuelType);
        if (type != null) return type;

        //Workaround to allow rovers from previous versions with badly formed components to load
        //e.g "hydrogen_bucket" as fuel_type

        ResourceLocation itemLoc = ResourceLocation.tryParse(fuelType);
        if (itemLoc == null) return FuelType.Type.FUEL;

        Optional<Item> item = BuiltInRegistries.ITEM.getOptional(itemLoc);
        if (item.isEmpty()) return FuelType.Type.FUEL;

        type = FuelType.Type.getTypeBasedOnItem(item.get());
        if (type != null) return type;

        return FuelType.Type.FUEL;
    }

    public TankUpgrade getTankUpgrade() {
        return new TankUpgrade(tankCapacity);
    }

    public static RoverComponent fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new RoverComponent(buffer.readUtf(), buffer.readInt(), buffer.readResourceLocation(), buffer.readInt(), buffer.readFloat());
    }

    public RegistryFriendlyByteBuf toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeUtf(this.fuelType);
        buffer.writeInt(this.fuel);
        buffer.writeResourceLocation(this.fuelTexture);
        buffer.writeInt(this.tankCapacity);
        buffer.writeFloat(this.speedModifier);
        return buffer;
    }

    public int getTankCapacity() {
        return tankCapacity;
    }

    public SpeedUpgrade getSpeedUpgrade() {
        return new SpeedUpgrade(speedModifier);
    }

    static {
        STREAM_CODEC = StreamCodec.composite( ByteBufCodecs.STRING_UTF8, RoverComponent::fuelType, ByteBufCodecs.INT,
                RoverComponent::fuel, ResourceLocation.STREAM_CODEC, RoverComponent::fuelTexture,
                ByteBufCodecs.INT, RoverComponent::tankCapacity,ByteBufCodecs.FLOAT,RoverComponent::speedModifier,RoverComponent::new);
    }
}
