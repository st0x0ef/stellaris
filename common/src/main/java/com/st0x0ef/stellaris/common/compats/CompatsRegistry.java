package com.st0x0ef.stellaris.common.compats;

import com.st0x0ef.stellaris.platform.CompatPlatform;
import dev.architectury.platform.Platform;

import java.util.HashMap;
import java.util.Map;

public class CompatsRegistry {

    public static Map<String, ModCompat> COMPATS_REGISTRY = new HashMap<String, ModCompat>();



    public static void registerCompat(String modid, ModCompat compat) {
        COMPATS_REGISTRY.put(modid, compat);
    }

    public static ModCompat getCompat(String modid) {
        return COMPATS_REGISTRY.get(modid);
    }

    public static void init() {
        for (Map.Entry<String, ModCompat> entry : COMPATS_REGISTRY.entrySet()) {
            if(Platform.isModLoaded(entry.getKey())) {
                entry.getValue().init();
            }
        }
    }

    public static void register() {
        registerCompat("stellaris", new TestCompat());
        CompatPlatform.registerLoaderCompat();
        init();
    }
}
