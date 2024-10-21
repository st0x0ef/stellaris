package com.st0x0ef.stellaris.common.items;

import com.st0x0ef.stellaris.common.vehicle_upgrade.VehicleUpgrade;
import net.minecraft.world.item.Item;

public class RocketUpgradeItem extends Item {
    private final VehicleUpgrade upgrade;

    public RocketUpgradeItem(Properties properties, VehicleUpgrade upgrade) {
        super(properties);
        this.upgrade = upgrade;
    }

    public VehicleUpgrade getUpgrade() {
        return upgrade;
    }
}

