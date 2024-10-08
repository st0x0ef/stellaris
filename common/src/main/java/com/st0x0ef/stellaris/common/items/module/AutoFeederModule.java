package com.st0x0ef.stellaris.common.items.module;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AutoFeederModule extends Item implements SpaceSuitModule {

    public AutoFeederModule(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void tick(ItemStack stack, Level level, Player player) {

        if (player.getFoodData().needsFood()) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack foodStack = player.getInventory().getItem(i);
                FoodProperties foodProperties = foodStack.get(DataComponents.FOOD);
                if (foodProperties!=null) {
                    player.getFoodData().eat(foodProperties);
                    foodStack.consume(1, player);
                    break;
                }
            }
        }
    }

    @Override
    public Component displayName() {
        return Component.translatable("spacesuit.stellaris.auto_feeder");
    }

}
