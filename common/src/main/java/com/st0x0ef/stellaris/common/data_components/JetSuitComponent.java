package com.st0x0ef.stellaris.common.data_components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.armors.JetSuit;
import net.minecraft.nbt.CompoundTag;

import java.io.Serializable;

public record JetSuitComponent(JetSuit.ModeType type) implements Serializable {
    public static final Codec<JetSuitComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            JetSuit.ModeType.CODEC.fieldOf("type").forGetter(JetSuitComponent::type)
    ).apply(instance, JetSuitComponent::new));

    public static JetSuitComponent getData(CompoundTag tag) {
        return new JetSuitComponent(JetSuit.ModeType.fromInt(tag.getInt("mode")));
    }

    public void savaData(CompoundTag tag) {
        tag.putInt("mode", this.type.getMode());
    }
}
