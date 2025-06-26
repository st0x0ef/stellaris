package com.st0x0ef.stellaris.platform.fabric;

import com.st0x0ef.stellaris.fabric.mixin.RegistrarMixin;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.core.Registry;

public class RegistrarUtilPlatformImpl {
    public static <T> Registry<T> getBaseRegistry(Registrar<T> registrar) {
        return ((RegistrarMixin)registrar).getDelegate();
    }
}
