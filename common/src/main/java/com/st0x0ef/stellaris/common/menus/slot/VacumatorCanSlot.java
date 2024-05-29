package com.st0x0ef.stellaris.common.menus.slot;

import com.st0x0ef.stellaris.common.items.CanItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VacumatorCanSlot extends Slot {

    public VacumatorCanSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof CanItem;
    }
}
