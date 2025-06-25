package com.st0x0ef.stellaris.common.items.module;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class GravityNormalizerModule extends SpaceSuitModuleItem {

    public GravityNormalizerModule(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public MutableComponent displayName() {
        return Component.translatable("spacesuit.stellaris.gravity_normalizer");
    }
}
