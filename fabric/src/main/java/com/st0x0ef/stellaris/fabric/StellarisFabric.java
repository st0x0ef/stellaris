package com.st0x0ef.stellaris.fabric;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class StellarisFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Stellaris.init();

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> Stellaris.onDatapackSyncEvent(player));

        EntityRegistry.registerAttributes((type, builder) -> FabricDefaultAttributeRegistry.register(type.get(), builder.get()));
    }
}