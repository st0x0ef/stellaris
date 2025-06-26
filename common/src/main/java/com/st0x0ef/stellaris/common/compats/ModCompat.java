package com.st0x0ef.stellaris.common.compats;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ModCompat {

    void init();

    default String loader() {
        return "common";
    }


    @Retention(RetentionPolicy.RUNTIME)
    @interface MixinCompat {

        String modid();

    }
}
