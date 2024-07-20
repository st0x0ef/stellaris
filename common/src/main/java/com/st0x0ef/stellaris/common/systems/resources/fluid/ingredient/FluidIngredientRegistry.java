package com.st0x0ef.stellaris.common.systems.resources.fluid.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.st0x0ef.stellaris.common.systems.resources.fluid.ingredient.impl.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FluidIngredientRegistry {
    public static final Map<ResourceLocation, FluidIngredientType<?>> INGREDIENT_TYPES = new HashMap<>();
    public static final Codec<FluidIngredientType<?>> TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(FluidIngredientRegistry::decode, FluidIngredientType::id);
    public static final StreamCodec<ByteBuf, FluidIngredientType<?>> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(INGREDIENT_TYPES::get, FluidIngredientType::id);

    public static void init() {}

    static {
        register(BaseFluidIngredient.TYPE);
        register(ComponentFluidIngredient.TYPE);
        register(DifferenceFluidIngredient.TYPE);
        register(AllMatchFluidIngredient.TYPE);
        register(AnyMatchFluidIngredient.TYPE);
    }

    public static void register(FluidIngredientType<?> type) {
        INGREDIENT_TYPES.put(type.id(), type);
    }

    private static DataResult<? extends FluidIngredientType<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(INGREDIENT_TYPES.get(id)).map(DataResult::success).orElse(DataResult.error(() -> "No ritual component type found."));
    }
}
