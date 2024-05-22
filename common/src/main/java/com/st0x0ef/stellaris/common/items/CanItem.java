package com.st0x0ef.stellaris.common.items;


import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class CanItem extends Item {
    private FoodProperties foodProperties;
    private final int maxNutrition;

    public CanItem(Properties properties, int maxNutrition) {
        super(properties);
        this.maxNutrition = maxNutrition;
        this.foodProperties = new FoodProperties.Builder().nutrition(0).saturationModifier(0).alwaysEdible().build();
    }



    public void setFoodProperties(ItemStack stack, FoodProperties foodProperties) {
        this.foodProperties = foodProperties;
        stack.set(DataComponents.FOOD, foodProperties);

    }

    public static int getNutrition(ItemStack food) {
        return food.get(DataComponents.FOOD).nutrition() ;
    }

    public static float getSaturation(ItemStack food) {
        return food.get(DataComponents.FOOD).saturation() ;
    }

    public boolean addFoodIfPossible(ItemStack food) {

        if (this.foodProperties.nutrition() + getNutrition(food) <= maxNutrition) {
            setFoodProperties(food, new FoodProperties.Builder().nutrition(this.foodProperties.nutrition() + getNutrition(food)).saturationModifier(this.foodProperties.saturation() + getSaturation(food)).build());
            return true;
        }

        return false;
    }

    public int getMaxNutrition() {
        return maxNutrition;
    }

    public FoodProperties getFoodProperties() {
        return this.foodProperties;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        //FoodProperties properties = itemStack.get(DataComponents.FOOD);

        if (foodProperties.nutrition() > 0 && foodProperties.saturation() > 0) {
            list.add(Component.literal("Nutrition : " + foodProperties.nutrition() + "/" + getMaxNutrition()));
            list.add(Component.literal("Saturation : " + foodProperties.saturation()));
        } else {
            list.add(Component.translatable("tooltip.stellaris.can_item_empty"));
        }
    }

}
