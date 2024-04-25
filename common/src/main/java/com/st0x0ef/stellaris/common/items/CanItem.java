package com.st0x0ef.stellaris.common.items;


import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;

import java.util.List;

public class CanItem extends Item {
    private final int maxNutrition;

    public CanItem(Properties properties, int maxNutrition) {
        super(properties);
        this.maxNutrition = maxNutrition;
    }

    public int getNutrition(ItemStack stack) {
        return stack.get(DataComponents.FOOD).nutrition() ;

    }

    public void addNutrition(ItemStack stack, int nutrition) {
        stack.set(DataComponents.FOOD, new FoodProperties.Builder().nutrition(stack.get(DataComponents.FOOD).nutrition() +nutrition).build());
    }

    public int getMaxNutrition() {
        return maxNutrition;

    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.literal("Nutrition : " + getNutrition(itemStack) + "/" + getMaxNutrition()));
    }

}
