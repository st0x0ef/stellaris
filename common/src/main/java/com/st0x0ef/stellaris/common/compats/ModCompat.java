package com.st0x0ef.stellaris.common.compats;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ModCompat {

    void init();


    @Retention(RetentionPolicy.RUNTIME)
    @interface Compat {

        /**
         * The mod id of the mod that this compat is for.
         * This is used to check if the mod is loaded and to register the compat.
         */
        String modid();

        /**
         * The platform that this compat is for.
         * This is used to check if the platform is correct.
         */
        String platform() default "common";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface MixinCompat {

        String modid();

    }
}
