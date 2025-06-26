package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.module.SpaceSuitModule;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;

public class RegistryRegistry {

    // MODULES
    public static final Registrar<SpaceSuitModule> SUIT_MODULE;


    public static void register() {
        SUIT_MODULE.key();
    }

    static {
        SUIT_MODULE = RegistrarManager.get(Stellaris.MODID).<SpaceSuitModule>builder(Stellaris.id("suit_module")).syncToClients().build();
    }
}
