package com.st0x0ef.stellaris.common.items.module;

import com.st0x0ef.stellaris.common.module.Module;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class GravityNormalizerModule extends SpaceSuitModuleItem {

    public GravityNormalizerModule(Properties properties) {
        super(properties);
    }

    @Override
    public MutableComponent displayName() {
        return Component.translatable("spacesuit.stellaris.gravity_normalizer");
    }

    @Override
    public <T extends Item & Module> Supplier<T> getAsItem() {
        return (Supplier<T>) ItemsRegistry.MODULE_GRAVITY_NORMALIZER;
    }
}
