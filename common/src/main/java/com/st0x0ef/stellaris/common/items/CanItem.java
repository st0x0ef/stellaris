package com.st0x0ef.stellaris.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CanItem extends Item {

    private final int maxNutrition;

    public CanItem(Properties properties, int maxNutrition) {
        super(properties);
        this.maxNutrition = maxNutrition;
    }

    public static void setFoodProperties(Item item, FoodProperties foodProperties) {
        item.foodProperties = foodProperties;
    }

    public static FoodProperties getFoodProperties(Item item) {
        return item.getFoodProperties();
    }

    public static int getNutrition(Item item) {
        FoodProperties properties = getFoodProperties(item);
        return properties != null ? properties.getNutrition() : 0;
    }

    public static float getSaturation(Item item) {
        FoodProperties properties = getFoodProperties(item);
        return properties != null ? properties.getSaturationModifier() : 0;
    }

    public static boolean addFoodToCan(ItemStack canStack, ItemStack foodStack) {
        int canNutrition = getNutrition(canStack.getItem()) + getNutrition(foodStack.getItem());
        if (canNutrition <= ((CanItem) canStack.getItem()).getMaxNutrition()) {
            setFoodProperties(canStack.getItem(), new FoodProperties.Builder().nutrition(canNutrition).saturationMod(Math.round((getSaturation(canStack.getItem()) + getSaturation(foodStack.getItem())) * 10F) / 10F).build());
            return true;
        }
        return false;
    }

    public int getMaxNutrition() {
        return maxNutrition;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltip, isAdvanced);

        FoodProperties properties = getFoodProperties(stack.getItem());

        if (properties == null || (properties.getNutrition() <= 0 && properties.getSaturationModifier() <= 0)) {
            tooltip.add(Component.translatable("tooltip.item.stellaris.can.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        if (properties.getNutrition() > 0) {
            tooltip.add(Component.translatable("tooltip.item.stellaris.can.nutrition", properties.getNutrition(), getMaxNutrition()).withStyle(ChatFormatting.GRAY));
        }

        if (properties.getSaturationModifier() > 0) {
            tooltip.add(Component.translatable("tooltip.item.stellaris.can.saturation", properties.getSaturationModifier()).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        super.finishUsingItem(stack, level, entity);
        if (entity instanceof Player player) {
            ItemStack emptyCanStack = new ItemStack(stack.getItem());
            if (stack.isEmpty()) {
                return emptyCanStack;
            }

            if (!player.getInventory().add(emptyCanStack)) {
                player.drop(emptyCanStack, false);
            }
        }
        return stack;
    }
}
