package com.st0x0ef.stellaris.common.module;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.io.Serializable;
import java.util.Set;
import java.util.function.Supplier;

public interface Module extends Serializable {

    Component displayName();

    <T extends Item & Module> Supplier<T> getAsItem();

    default Set<? extends Module> requires() {
        return Set.of();
    }

    default Set<? extends Module> incompatible() {
        return Set.of();
    }

}
