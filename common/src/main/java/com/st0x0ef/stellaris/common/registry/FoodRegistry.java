package com.st0x0ef.stellaris.common.registry;

import net.minecraft.world.food.FoodProperties;

public class FoodRegistry {
    //**FOOD REGiSTRY**//
    public static final FoodProperties CHEESE = new FoodProperties.Builder().nutrition(3).saturationMod(1).build();
    public static final FoodProperties CANNED_STEAK = (new FoodProperties.Builder()).nutrition(8).saturationMod(0.8F).meat().build();
    public static final FoodProperties BERRY_JUICE = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.4F).meat().build();
    public static final FoodProperties COSMO_BREAD = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.6F).meat().build();


}
