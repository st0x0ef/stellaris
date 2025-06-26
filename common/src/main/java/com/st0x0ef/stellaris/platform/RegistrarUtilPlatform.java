package com.st0x0ef.stellaris.platform;

import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.core.Registry;

public class RegistrarUtilPlatform {

    public static <T> Codec<T> getByNameCodec(Registrar<T> registrar) {
        return getBaseRegistry(registrar).byNameCodec();
    }

    @ExpectPlatform
    public static <T> Registry<T> getBaseRegistry(Registrar<T> registrar) {
        throw new AssertionError();
    }
}
