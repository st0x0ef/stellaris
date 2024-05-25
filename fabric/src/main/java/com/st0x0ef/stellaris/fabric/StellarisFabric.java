package com.st0x0ef.stellaris.fabric;

import com.st0x0ef.stellaris.Stellaris;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class StellarisFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Stellaris.init();

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> Stellaris.onDatapackSyncEvent(player));

    }
}