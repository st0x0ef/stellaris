package com.st0x0ef.stellaris.common.menus.slot.upgrade;

import com.st0x0ef.stellaris.common.items.RocketUpgradeItem;
import com.st0x0ef.stellaris.common.rocket_upgrade.SkinUpgrade;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RocketSkinSlot extends Slot {

    public RocketSkinSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.getItem() instanceof RocketUpgradeItem item) {
            return item.getUpgrade() instanceof SkinUpgrade;
        }

        return false;
    }
}