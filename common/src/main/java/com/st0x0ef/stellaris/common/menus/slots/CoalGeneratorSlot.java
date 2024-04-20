package com.st0x0ef.stellaris.common.menus.slots;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CoalGeneratorSlot extends Slot {

    public CoalGeneratorSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public void set(ItemStack stack) {
        if(stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL){
            super.set(stack);
        }
    }
}
