package com.st0x0ef.stellaris.common.systems.resources.entity.ingredient.impl;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.systems.resources.ResourceLib;
import com.st0x0ef.stellaris.common.systems.resources.entity.EntityResource;
import com.st0x0ef.stellaris.common.systems.resources.entity.ingredient.EntityIngredient;
import com.st0x0ef.stellaris.common.systems.resources.entity.ingredient.EntityIngredientType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record DifferenceEntityIngredient(EntityIngredient minuend, EntityIngredient subtrahend) implements EntityIngredient {
    public static final MapCodec<DifferenceEntityIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EntityIngredient.CODEC.fieldOf("minuend").forGetter(DifferenceEntityIngredient::minuend),
            EntityIngredient.CODEC.fieldOf("subtrahend").forGetter(DifferenceEntityIngredient::subtrahend)
    ).apply(instance, DifferenceEntityIngredient::new));

    public static final EntityIngredientType<DifferenceEntityIngredient> TYPE = new EntityIngredientType<>(new ResourceLocation(ResourceLib.MOD_ID, "difference"), CODEC);

    @Override
    public List<EntityResource> getMatchingEntities() {
        List<EntityResource> stacks = new ArrayList<>(minuend.getMatchingEntities());
        stacks.removeIf(subtrahend);
        return stacks;
    }

    @Override
    public boolean requiresTesting() {
        return minuend.requiresTesting() || subtrahend.requiresTesting();
    }

    @Override
    public boolean test(EntityResource fluidResource) {
        return minuend.test(fluidResource) && !subtrahend.test(fluidResource);
    }

    @Override
    public EntityIngredientType<?> getType() {
        return TYPE;
    }
}
