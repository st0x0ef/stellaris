package com.st0x0ef.stellaris.platform.neoforge;

import com.st0x0ef.stellaris.neoforge.mixin.RegistrarMixin;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.core.Registry;

public class RegistrarUtilPlatformImpl {
    public static <T> Registry<T> getBaseRegistry(Registrar<T> registrar) {
        return ((RegistrarMixin)registrar).getDelegate();
    }
}
