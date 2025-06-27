package com.st0x0ef.stellaris.platform.neoforge;

import com.st0x0ef.stellaris.common.compats.CompatsRegistry;
import com.st0x0ef.stellaris.neoforge.compat.MekanismCompat;
import net.neoforged.fml.loading.LoadingModList;

public class CompatPlatformImpl {

    public static boolean isModLoading(String modid) {
        return LoadingModList.get().getModFileById(modid) != null;
    }

    public static void registerLoaderCompat() {
        CompatsRegistry.registerCompat("mekanism", new MekanismCompat());
    }

}
