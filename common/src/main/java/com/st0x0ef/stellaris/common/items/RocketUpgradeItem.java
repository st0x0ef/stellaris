package com.st0x0ef.stellaris.common.items;

import com.st0x0ef.stellaris.common.rocket_upgrade.RocketUpgrade;
import net.minecraft.world.item.Item;

public class RocketUpgradeItem extends Item {
    private final RocketUpgrade upgrade;

    public RocketUpgradeItem(Properties properties, RocketUpgrade upgrade) {
        super(properties);
        this.upgrade = upgrade;
    }

    public RocketUpgrade getUpgrade() {
        return upgrade;
    }
}

