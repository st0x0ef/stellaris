package com.st0x0ef.stellaris.common.module;

import net.minecraft.network.chat.Component;

import java.io.Serializable;
import java.util.Set;

public interface Module extends Serializable {

    Component displayName();

    default Set<? extends Module> requires() {
        return Set.of();
    }

    default Set<? extends Module> incompatible() {
        return Set.of();
    }

}
