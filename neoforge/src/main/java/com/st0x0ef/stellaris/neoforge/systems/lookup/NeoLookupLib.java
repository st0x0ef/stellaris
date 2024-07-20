package com.st0x0ef.stellaris.neoforge.systems.lookup;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

public class NeoLookupLib {
    public NeoLookupLib(IEventBus event) {
        event.addListener(RegistryEventListener::registerAll);
    }
}
