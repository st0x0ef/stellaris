package com.st0x0ef.stellaris.common.network;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.network.packets.*;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;

public interface NetworkRegistry {
    SimpleNetworkManager NETWORK_MANAGER = SimpleNetworkManager.create(Stellaris.MODID);
    
    MessageType KEY_HANDLER_ID = NETWORK_MANAGER.registerC2S("key_handler", KeyHandlerPacket::new);
    MessageType TELEPORT_ENTITY_ID = NETWORK_MANAGER.registerC2S("teleport_entity", TeleportEntityToPlanetPacket::new);

    MessageType SYNC_PLANETS_DATAPACK = NETWORK_MANAGER.registerS2C("sync_planet_datapack", SyncPlanetsDatapackPacket::new);
    MessageType SYNC_FLUID_TANKS_ID = NETWORK_MANAGER.registerS2C("sync_fluid_tanks", SyncWidgetsTanksPacket::new);
    MessageType SYNC_ROCKET_COMPONENT_ID = NETWORK_MANAGER.registerS2C("sync_rocket_component", SyncRocketComponentPacket::new);
}
