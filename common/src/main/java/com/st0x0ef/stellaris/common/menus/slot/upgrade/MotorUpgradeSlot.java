package com.st0x0ef.stellaris.common.menus.slot.upgrade;

import com.st0x0ef.stellaris.common.items.RocketUpgradeItem;
import com.st0x0ef.stellaris.common.menus.RocketMenu;
import com.st0x0ef.stellaris.common.vehicle_upgrade.MotorUpgrade;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MotorUpgradeSlot extends Slot {

    public MotorUpgradeSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.getItem() instanceof RocketUpgradeItem item) {
            return item.getUpgrade() instanceof MotorUpgrade;
        }

        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        if (player.containerMenu instanceof RocketMenu menu) {
            return menu.getRocket().getFuel() == 0;
        }

        return false;
    }
}