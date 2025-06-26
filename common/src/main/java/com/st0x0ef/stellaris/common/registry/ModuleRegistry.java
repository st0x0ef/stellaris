package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.module.SpaceSuitModule;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;

import java.util.function.Supplier;

public abstract class ModuleRegistry {
    public static final Registrar<SpaceSuitModule> SUIT_MODULE;

    public static class SpaceSuitModules extends ModuleRegistry {
        public static final RegistrySupplier<? extends SpaceSuitModule> AUTO_FEEDER_MODULE =
                register("auto_feeder", ItemsRegistry.MODULE_AUTO_FEEDER);
        public static final RegistrySupplier<? extends SpaceSuitModule> OIL_FINDER_MODULE =
                register("oil_finder", ItemsRegistry.MODULE_OIL_FINDER);
        public static final RegistrySupplier<? extends SpaceSuitModule> GRAVITY_NORMALIZER_MODULE =
                register("gravity_normalizer", ItemsRegistry.MODULE_GRAVITY_NORMALIZER);
        public static final RegistrySupplier<? extends SpaceSuitModule> FUEL_MODULE =
                register("gravity_normalizer", ItemsRegistry.MODULE_FUEL);
        public static final RegistrySupplier<? extends SpaceSuitModule> JET_MODULE =
                register("gravity_normalizer", ItemsRegistry.MODULE_JET);


        private static RegistrySupplier<? extends SpaceSuitModule> register(String id, Supplier<? extends SpaceSuitModule> supplier) {
            return SUIT_MODULE.register(Stellaris.id(id), supplier);
        }
    }


    static {
        SUIT_MODULE = RegistrarManager.get(Stellaris.MODID).<SpaceSuitModule>builder(Stellaris.id("suit_module"))
                .syncToClients()
                .build();
    }
}
