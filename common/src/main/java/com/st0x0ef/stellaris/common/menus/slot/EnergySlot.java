package com.st0x0ef.stellaris.common.menus.slot;

import com.fej1fun.potentials.capabilities.Capabilities;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EnergySlot extends Slot {
    public EnergySlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return Capabilities.Energy.ITEM.getCapability(stack) != null;
    }
}
