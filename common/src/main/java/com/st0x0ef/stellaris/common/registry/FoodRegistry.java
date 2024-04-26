package com.st0x0ef.stellaris.common.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class FoodRegistry {
    public static final FoodProperties CHEESE = new FoodProperties.Builder().nutrition(3).saturationModifier(1).build();
    public static final FoodProperties CANNED_STEAK = (new FoodProperties.Builder()).nutrition(8).saturationModifier(0.8F).build();
    public static final FoodProperties BERRY_JUICE = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.4F).build();
    public static final FoodProperties COSMO_BREAD = (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.4F).build();
}
