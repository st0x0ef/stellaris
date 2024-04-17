package com.st0x0ef.stellaris.common.items;


import com.st0x0ef.stellaris.common.entities.IceShardArrowEntity;
import com.st0x0ef.stellaris.mixin.FoodPropertiesAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CanItem extends Item {
    private final int maxNutrition;

    public CanItem(Properties properties, int maxNutrition) {
        super(properties);
        this.maxNutrition = maxNutrition;
    }

    @Override
    public boolean isEdible() {
        return true;
    }

    public int getNutrition(ItemStack stack) {
        return stack.getOrCreateTag().getInt("nutrition");
    }

    public void addNutrition(ItemStack stack, int nutrition) {
        stack.getOrCreateTag().putInt("nutrition", nutrition + getNutrition(stack));
        ((FoodPropertiesAccessor) this).setFoodProperties(new FoodProperties.Builder().nutrition(getNutrition(stack)).saturationMod(0.3F).build());
    }

    public int getMaxNutrition() {
        return maxNutrition;

    }

    @Nullable
    @Override
    public FoodProperties getFoodProperties() {
        return new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).build();
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.literal("Nutrition : " + getNutrition(itemStack) + "/" + getMaxNutrition()));
    }
}
