package com.st0x0ef.stellaris.common.items;


import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;

import java.util.List;

public class CanItem extends Item {
    private FoodProperties foodProperties;
    private final int maxNutrition;

    public CanItem(Properties properties, int maxNutrition) {
        super(properties);
        this.maxNutrition = maxNutrition;
        this.foodProperties = new FoodProperties.Builder().nutrition(0).saturationModifier(0).build();
    }

    public void setFoodProperties(FoodProperties foodProperties) {
        this.foodProperties = foodProperties;
    }

    public static int getNutrition(ItemStack food) {
        return food.get(DataComponents.FOOD).nutrition() ;
    }

    public static float getSaturation(ItemStack food) {
        return food.get(DataComponents.FOOD).saturation() ;
    }

    public boolean addFoodIfPossible(ItemStack food) {
        if (foodProperties.nutrition() + getNutrition(food) <= maxNutrition) {
            setFoodProperties(new FoodProperties.Builder().nutrition(this.foodProperties.nutrition() + getNutrition(food)).saturationModifier(this.foodProperties.saturation() + getSaturation(food)).build());
            return true;
        }

        return false;
    }

    public int getMaxNutrition() {
        return maxNutrition;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        if (foodProperties.nutrition() > 0 && foodProperties.saturation() > 0) {
            list.add(Component.literal("Nutrition : " + foodProperties.nutrition() + "/" + getMaxNutrition()));
            list.add(Component.literal("Saturation : " + foodProperties.saturation()));
        } else {
            list.add(Component.translatable("tooltip.stellaris.can_item_empty"));
        }
    }
}
