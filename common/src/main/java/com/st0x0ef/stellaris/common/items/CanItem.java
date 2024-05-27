package com.st0x0ef.stellaris.common.items;


import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CanItem extends Item {
    final private int maxNutrition;

    public CanItem(Properties properties, int maxNutrition) {
        super(properties);
        this.maxNutrition = maxNutrition;
    }



    public void setFoodProperties(ItemStack stack, FoodProperties foodProperties) {
        System.out.println("New food properties " + foodProperties.nutrition() + " " + foodProperties.saturation());
        stack.set(DataComponents.FOOD, foodProperties);


    }

    public static int getNutrition(ItemStack food) {
        return food.get(DataComponents.FOOD).nutrition() ;
    }

    public static float getSaturation(ItemStack food) {
        return food.get(DataComponents.FOOD).saturation() ;
    }

    public boolean addFoodIfPossible(ItemStack can, ItemStack food) {
        FoodProperties canFoodProperties = can.get(DataComponents.FOOD);
        FoodProperties foodProperties = food.get(DataComponents.FOOD);

        //System.out.println(foodProperties.nutrition() + getNutrition(food) + " " + maxNutrition);


        if (foodProperties.nutrition() + getNutrition(can) <= maxNutrition) {
            System.out.println("added  " + getNutrition(can) + "  " + canFoodProperties.nutrition());

            setFoodProperties(food, new FoodProperties.Builder().nutrition(foodProperties.nutrition() + getNutrition(can)).saturationModifier(foodProperties.saturation() + getSaturation(can)).build());
            return true;
        }

        return false;
    }

    public int getMaxNutrition() {
        return maxNutrition;
    }


    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        FoodProperties properties = itemStack.get(DataComponents.FOOD);

        if(properties == null) {
            System.out.println("null");
            list.add(Component.translatable("tooltip.stellaris.can_item_empty"));
        }

        if (properties.nutrition() > 0 && properties.saturation() > 0) {
            list.add(Component.literal("Nutrition : " + properties.nutrition() + "/" + getMaxNutrition()));
            list.add(Component.literal("Saturation : " + properties.saturation()));
        } else {
            list.add(Component.translatable("tooltip.stellaris.can_item_empty"));
        }
    }

}
