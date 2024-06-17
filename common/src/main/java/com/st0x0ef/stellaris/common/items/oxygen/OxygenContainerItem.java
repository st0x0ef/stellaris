package com.st0x0ef.stellaris.common.items.oxygen;

import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;
import net.minecraft.world.item.Item;

public abstract class OxygenContainerItem extends Item {
    protected OxygenContainer container;

    public OxygenContainerItem(Properties properties, OxygenContainer container) {
        super(properties);

        this.container = container;
    }

    public OxygenContainer getOxygenContainer() {
        return container;
    }
}
