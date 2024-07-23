package com.st0x0ef.stellaris.platform.systems.resources.fluid.ingredient;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.ingredient.impl.*;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public interface FluidIngredient extends Predicate<FluidResource> {
    Codec<FluidIngredient> TYPE_CODEC = FluidIngredientRegistry.TYPE_CODEC.dispatch(FluidIngredient::getType, FluidIngredientType::codec);
    Codec<FluidIngredient> CODEC = Codec.either(BaseFluidIngredient.CODEC, TYPE_CODEC).xmap(either -> either.map(l -> l, r -> r), ingredient -> ingredient instanceof BaseFluidIngredient ? Either.left((BaseFluidIngredient) ingredient) : Either.right(ingredient));
    MapCodec<FluidIngredient> MAP_CODEC = FluidIngredientRegistry.TYPE_CODEC.dispatchMap(FluidIngredient::getType, FluidIngredientType::codec);

    @SuppressWarnings("unchecked")
    StreamCodec<RegistryFriendlyByteBuf, FluidIngredient> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf regByteBuf, FluidIngredient ingredient) {
            FluidIngredientRegistry.STREAM_CODEC.encode(regByteBuf, ingredient.getType());
            ((StreamCodec<RegistryFriendlyByteBuf, FluidIngredient>) ingredient.getType().streamCodec()).encode(regByteBuf, ingredient);
        }

        @Override
        public @NotNull FluidIngredient decode(RegistryFriendlyByteBuf regByteBuf) {
            FluidIngredientType<?> fluidIngredientType = FluidIngredientRegistry.STREAM_CODEC.decode(regByteBuf);
            return fluidIngredientType.streamCodec().decode(regByteBuf);
        }
    };

    static FluidIngredient of(FluidResource... stacks) {
        return BaseFluidIngredient.of(stacks);
    }

    static FluidIngredient of(TagKey<Fluid> tag) {
        return BaseFluidIngredient.of(tag);
    }

    static FluidIngredient all(FluidIngredient... ingredients) {
        return new AllMatchFluidIngredient(Arrays.asList(ingredients));
    }

    static FluidIngredient any(FluidIngredient... ingredients) {
        return new AnyMatchFluidIngredient(Arrays.asList(ingredients));
    }

    static FluidIngredient difference(FluidIngredient minuend, FluidIngredient subtrahend) {
        return new DifferenceFluidIngredient(minuend, subtrahend);
    }

    static FluidIngredient components(FluidIngredient base, DataComponentPredicate components) {
        return new ComponentFluidIngredient(base, components);
    }

    static SizedFluidIngredient sized(FluidIngredient ingredient, long size) {
        return new SizedFluidIngredient(ingredient, size);
    }

    boolean test(FluidResource fluidResource);

    List<FluidResource> getMatchingFluids();

    boolean requiresTesting();

    FluidIngredientType<?> getType();
}
