package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankHelper;
import net.minecraft.network.FriendlyByteBuf;

import java.io.Serializable;

public record SpaceArmorComponent(long fuel, int oxygen) implements Serializable {

    public static final long MAX_CAPACITY = FluidTankHelper.convertFromMb(3000);

    public static final Codec<SpaceArmorComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("fuel").forGetter(SpaceArmorComponent::fuel),
            Codec.INT.fieldOf("oxygen").forGetter(SpaceArmorComponent::oxygen)
    ).apply(instance, SpaceArmorComponent::new));
    public static SpaceArmorComponent fromNetwork(FriendlyByteBuf buffer) {
        return new SpaceArmorComponent(buffer.readInt(), buffer.readInt());
    }

    public FriendlyByteBuf toNetwork(FriendlyByteBuf buffer) {
        buffer.writeLong(this.fuel);
        buffer.writeInt(this.oxygen);
        return buffer;
    }
}
