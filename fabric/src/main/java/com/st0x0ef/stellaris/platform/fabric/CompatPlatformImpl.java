package com.st0x0ef.stellaris.platform.fabric;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.compats.ModCompat;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class CompatPlatformImpl {

    public static void loadCompats() {

        FabricLoader loader = FabricLoader.getInstance();

        List<ModCompat> compats = loader.getEntrypoints("stellaris", ModCompat.class);

        for(ModCompat compat : compats) {
            ModCompat.Compat annotation = compat.getClass().getAnnotation(ModCompat.Compat.class);
            if (annotation != null && loader.isModLoaded(annotation.modid())) {
                try {
                    compat.init();
                } catch (Exception e) {
                    Stellaris.LOG.error("Failed to initialize compat: " + annotation.modid(), e);
                }
            }
        }

    }

}
