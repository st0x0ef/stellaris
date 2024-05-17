package com.st0x0ef.stellaris.common.menus.slot;

import com.st0x0ef.stellaris.common.items.oxygen.OxygenContainerItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OxygenTankSlot extends Slot {
    public OxygenTankSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.getItem() instanceof OxygenContainerItem;
    }

    public int getMaxStackSize() {
        return 1;
    }
}
