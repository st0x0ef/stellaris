package com.st0x0ef.stellaris.platform.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class CompatPlatformImpl {

    public static boolean isModLoading(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    public static void registerLoaderCompat() {
    }

}
