package com.st0x0ef.stellaris.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class CompatPlatform {

    @ExpectPlatform
    public static boolean isModLoading(String modid) {
        throw new AssertionError();
    }

}
