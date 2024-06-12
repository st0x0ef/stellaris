package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import com.st0x0ef.stellaris.common.rocket_upgrade.FuelType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.io.Serializable;

public record SpaceArmorComponent(int fuel, int oxygen) implements Serializable {

    public static final Codec<SpaceArmorComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("fuel").forGetter(SpaceArmorComponent::fuel),
            Codec.INT.fieldOf("oxygen").forGetter(SpaceArmorComponent::oxygen)
    ).apply(instance, SpaceArmorComponent::new));

    public static final StreamCodec<ByteBuf, SpaceArmorComponent> STREAM_CODEC;


    public int getFuel() {
        return fuel;
    }

    public int getOxygen() {
        return oxygen;
    }


    public static SpaceArmorComponent fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new SpaceArmorComponent(buffer.readInt(), buffer.readInt());
    }

    public RegistryFriendlyByteBuf toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.fuel);
        buffer.writeInt(this.oxygen);
        return buffer;
    }


    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, SpaceArmorComponent::fuel, ByteBufCodecs.INT, SpaceArmorComponent::oxygen, SpaceArmorComponent::new);
    }
}
