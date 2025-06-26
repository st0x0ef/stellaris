package com.st0x0ef.stellaris.platform.neoforge;

import net.neoforged.fml.loading.LoadingModList;

public class CompatPlatformImpl {

    public static boolean isModLoading(String modid) {
        return LoadingModList.get().getModFileById(modid) != null;
    }
}
