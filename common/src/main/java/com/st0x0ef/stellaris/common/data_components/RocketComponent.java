package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rocket.RocketModel;
import com.st0x0ef.stellaris.common.rocket_upgrade.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.io.Serializable;

public record RocketComponent(String skin, RocketModel model, String fuelType, int fuel, int tankCapacity) implements Serializable {

    public static final Codec<RocketComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("skin").forGetter(RocketComponent::skin),
            RocketModel.CODEC.fieldOf("model").forGetter(RocketComponent::model),
            Codec.STRING.fieldOf("fuel_type").forGetter(RocketComponent::fuelType),
            Codec.INT.fieldOf("fuel").forGetter(RocketComponent::fuel),
            Codec.INT.fieldOf("fuel_capacity").forGetter(RocketComponent::tankCapacity)
    ).apply(instance, RocketComponent::new));


    public int getFuel() {
        return fuel;
    }

    public ResourceLocation getSkin() {
        return new ResourceLocation(skin);
    }

    public SkinUpgrade getSkinUpgrade() {
        return new SkinUpgrade(getSkin()) ;
    }

    public RocketModel getModel() {
        return model;
    }
    public ModelUpgrade getModelUpgrade() {
        return new ModelUpgrade(model) ;
    }

    public MotorUpgrade getMotorUpgrade() {
        return new MotorUpgrade(FuelType.Type.fromString(fuelType));
    }

    public TankUpgrade getTankUpgrade() {
        return new TankUpgrade(tankCapacity);
    }


    public int getTankCapacity() {
        return tankCapacity;
    }

    public static RocketComponent fromNetwork(FriendlyByteBuf buffer) {
        return new RocketComponent(buffer.readUtf(), RocketModel.fromString(buffer.readUtf()), buffer.readUtf(), buffer.readInt(), buffer.readInt());
    }

    public FriendlyByteBuf toNetwork(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.skin);
        buffer.writeUtf(this.model().getSerializedName());
        buffer.writeUtf(this.fuelType);
        buffer.writeInt(this.fuel);
        buffer.writeInt(this.tankCapacity);
        return buffer;
    }
}
