package com.st0x0ef.stellaris.common.utils;

import com.st0x0ef.stellaris.common.data_components.OxygenComponent;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import net.minecraft.world.item.ItemStack;

public class OxygenUtils {
    public static void addOxygen(ItemStack stack, long amount) {
        stack.set(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get(), new OxygenComponent(stack.get(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get()).oxygen() + amount, stack.get(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get()).max()));
    }

    public static void setOxygen(ItemStack stack, long amount) {
        stack.set(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get(), new OxygenComponent(amount, stack.get(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get()).max()));
    }

    public static long getOxygen(ItemStack stack) {
        if (stack.has(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get())) {
            return stack.get(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get()).oxygen();
        }
        return 0L;
    }

    public static long getOxygenCapacity(ItemStack stack) {
        if (stack.has(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get())) {
            return stack.get(DataComponentsRegistry.STORED_OXYGEN_COMPONENT.get()).max();
        }
        return 0L;
    }
}
