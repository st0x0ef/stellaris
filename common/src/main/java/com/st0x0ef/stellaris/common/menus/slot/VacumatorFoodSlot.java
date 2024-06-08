package com.st0x0ef.stellaris.common.menus.slot;

import com.st0x0ef.stellaris.common.blocks.entities.machines.VacumatorBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VacumatorFoodSlot extends Slot {

    public VacumatorFoodSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return VacumatorBlockEntity.isFood(stack);
    }
}
