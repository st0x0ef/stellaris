package com.st0x0ef.stellaris.common.menus;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ResultSlot extends Slot {
    public ResultSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    public int getMaxStackSize() {
        return 64;
    }
}
