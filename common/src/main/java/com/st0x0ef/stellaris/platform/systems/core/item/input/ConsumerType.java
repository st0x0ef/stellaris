package com.st0x0ef.stellaris.platform.systems.core.item.input;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ConsumerType<T extends ItemConsumer>(ResourceLocation id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
    public ConsumerType(ResourceLocation location, MapCodec<T> codec) {
        this(location, codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()));
    }
}
