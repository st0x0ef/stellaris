package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.io.Serializable;

public record SpaceArmorComponent(long fuel, int oxygen) implements Serializable {

    public static final long MAX_CAPACITY = FluidTankHelper.convertFromMb(3000);

    public static final Codec<SpaceArmorComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("fuel").forGetter(SpaceArmorComponent::fuel),
            Codec.INT.fieldOf("oxygen").forGetter(SpaceArmorComponent::oxygen)
    ).apply(instance, SpaceArmorComponent::new));

    public static final StreamCodec<ByteBuf, SpaceArmorComponent> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_LONG, SpaceArmorComponent::fuel, ByteBufCodecs.INT, SpaceArmorComponent::oxygen, SpaceArmorComponent::new);

    public static SpaceArmorComponent fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new SpaceArmorComponent(buffer.readInt(), buffer.readInt());
    }

    public RegistryFriendlyByteBuf toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeLong(this.fuel);
        buffer.writeInt(this.oxygen);
        return buffer;
    }
}
