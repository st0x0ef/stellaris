package com.st0x0ef.stellaris.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class RadioactiveBlockItem extends BlockItem implements RadiationItem {

    private final int radiationLevel;

    public RadioactiveBlockItem(Block block, Properties properties, int radiationLevel) {
        super(block, properties);
        this.radiationLevel = radiationLevel;
    }

    @Override
    public int getRadiationLevel() {
        return radiationLevel - 1;
    }
}
