package com.st0x0ef.stellaris.platform.systems.lookup;

import net.neoforged.bus.api.IEventBus;

public class NeoLookupLib {
    public NeoLookupLib(IEventBus event) {
        event.addListener(RegistryEventListener::registerAll);
    }
}
