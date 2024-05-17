package com.st0x0ef.stellaris.common.items.oxygen;

import com.st0x0ef.stellaris.common.oxygen.OxygenContainer;

public class OxygenTankItem extends OxygenContainerItem {

    public OxygenTankItem(Properties properties, int capacity) {
        super(properties, new OxygenContainer(capacity));
    }
}
