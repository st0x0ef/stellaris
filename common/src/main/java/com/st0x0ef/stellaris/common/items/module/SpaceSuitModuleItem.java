package com.st0x0ef.stellaris.common.items.module;

import com.st0x0ef.stellaris.common.module.SpaceSuitModule;
import net.minecraft.world.item.Item;

public abstract class SpaceSuitModuleItem extends Item implements SpaceSuitModule {
    public SpaceSuitModuleItem(Properties properties) {
        super(properties);
    }
}
