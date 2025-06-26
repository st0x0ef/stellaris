package com.st0x0ef.stellaris.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class CompatPlatform {

    @ExpectPlatform
    public static void loadCompats() {
        throw new AssertionError();
    }

}
