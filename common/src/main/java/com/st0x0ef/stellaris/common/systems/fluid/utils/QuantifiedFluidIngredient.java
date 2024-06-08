package com.st0x0ef.stellaris.common.systems.fluid.utils;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.platform.systems.fluid.FluidConstants;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;

import java.util.List;

public class QuantifiedFluidIngredient extends FluidIngredient {
    public static final Codec<QuantifiedFluidIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FluidIngredient.CODEC.fieldOf("ingredient").forGetter(QuantifiedFluidIngredient::getIngredient),
            Codec.LONG.fieldOf("millibuckets").orElse(1000L).forGetter(o -> FluidConstants.toMillibuckets(o.getFluidAmount()))
    ).apply(instance, (fluidIngredient, aLong) -> new QuantifiedFluidIngredient(fluidIngredient, FluidConstants.fromMillibuckets(aLong))));

    private final long fluidAmount;
    private final FluidIngredient ingredient;

    public QuantifiedFluidIngredient(FluidIngredient ingredient, long fluidAmount) {
        super(ingredient.getRawValues());
        this.ingredient = ingredient;
        this.fluidAmount = fluidAmount;
    }

    public long getFluidAmount() {
        return fluidAmount;
    }

    public FluidIngredient getIngredient() {
        return ingredient;
    }

    @Override
    public boolean test(FluidHolder fluidHolder) {
        return ingredient.test(fluidHolder) && fluidHolder.getFluidAmount() >= this.fluidAmount;
    }

    @Override
    public List<FluidHolder> getFluids() {
        return ingredient.getFluids();
    }

    @Override
    public List<Either<FluidValue, TagValue>> getRawValues() {
        return ingredient.getRawValues();
    }
}
