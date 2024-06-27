package com.st0x0ef.stellaris.common.items;

import net.minecraft.world.item.Item;

public class RadioactiveItem extends Item implements RadiationItem {

    private final int radiationLevel;

    public RadioactiveItem(Properties properties, int radiationLevel) {
        super(properties);
        this.radiationLevel = radiationLevel;
    }

    @Override
    public int getRadiationLevel() {
        return radiationLevel;
    }

    @Override
    public boolean isBlock() {
        return false;
    }
}
