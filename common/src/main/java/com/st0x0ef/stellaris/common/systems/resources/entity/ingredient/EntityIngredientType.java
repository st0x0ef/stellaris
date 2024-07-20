package com.st0x0ef.stellaris.common.systems.resources.entity.ingredient;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record EntityIngredientType<T extends EntityIngredient>(ResourceLocation id, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
    public EntityIngredientType(ResourceLocation id, MapCodec<T> codec) {
        this(id, codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()));
    }
}
