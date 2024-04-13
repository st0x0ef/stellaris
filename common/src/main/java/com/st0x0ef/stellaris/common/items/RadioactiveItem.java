package com.st0x0ef.stellaris.common.items;

import net.minecraft.world.item.Item;

public class RadioactiveItem extends Item{
    private final int radiationLevel;

    public RadioactiveItem(Properties properties, int radiationLevel) {
        super(properties);
        this.radiationLevel = radiationLevel;
    }

    public int getRadiationLevel() {
        return radiationLevel - 1;
    }
}
