package com.st0x0ef.stellaris.common.menus.slots;

import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class UraniumGeneratorSlot extends Slot {

    public UraniumGeneratorSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public void set(ItemStack stack) {
        if(stack.getItem() == ItemsRegistry.URANIUM_INGOT.get()){
            super.set(stack);
        }
    }
}
