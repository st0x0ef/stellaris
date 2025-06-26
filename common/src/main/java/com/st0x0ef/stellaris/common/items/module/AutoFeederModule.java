package com.st0x0ef.stellaris.common.items.module;

import com.st0x0ef.stellaris.common.module.Module;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class AutoFeederModule extends SpaceSuitModuleItem {

    public AutoFeederModule(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(ItemStack stack, Level level, Player player) {

        if (player.getFoodData().needsFood()) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack foodStack = player.getInventory().getItem(i);
                FoodProperties foodProperties = foodStack.get(DataComponents.FOOD);
                if (foodProperties != null) {
                    if (PlanetUtil.hasOxygen(level) || foodStack.is(TagRegistry.SPACE_FOOD)) {
                        player.eat(level, foodStack, foodProperties);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public MutableComponent displayName() {
        return Component.translatable("spacesuit.stellaris.auto_feeder");
    }

    @Override
    public <T extends Item & Module> Supplier<T> getAsItem() {
        return (Supplier<T>) ItemsRegistry.MODULE_AUTO_FEEDER;
    }
}
