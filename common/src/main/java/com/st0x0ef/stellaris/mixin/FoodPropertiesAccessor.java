package com.st0x0ef.stellaris.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(Item.class)
public interface FoodPropertiesAccessor
{

    @Mutable
    @Accessor("foodProperties")
    public void setFoodProperties(FoodProperties foodProperties);
}
