package com.st0x0ef.stellaris.common.systems.resources.fluid.ingredient;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record FluidIngredientType<T extends FluidIngredient>(ResourceLocation id, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
    public FluidIngredientType(ResourceLocation id, MapCodec<T> codec) {
        this(id, codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()));
    }
}
