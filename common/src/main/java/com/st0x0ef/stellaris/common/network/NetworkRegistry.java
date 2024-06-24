package com.st0x0ef.stellaris.common.network;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.network.packets.*;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;

public class NetworkRegistry {

    public static SimpleNetworkManager NET = SimpleNetworkManager.create(Stellaris.MODID);

    // Server to client
    public static MessageType SYNC_PLANETS_DATAPACK = NET.registerS2C("sync_planet_datapack", SyncPlanetsDatapack::new);
    public static MessageType SYNC_ROCKET_COMPONENT_ID = NET.registerS2C("sync_rocket_component", SyncRocketComponent::new);
    public static MessageType SYNC_FLUID_TANKS_ID = NET.registerS2C("sync_fluid_tanks", SyncWidgetsTanks::new);

    // Server to client
    public static MessageType TELEPORT_ENTITY_ID = NET.registerC2S("teleport_entity", TeleportEntityToPlanet::new);
    public static MessageType KEY_HANDLER_ID = NET.registerC2S("key_handler", KeyHandler::new);


    public static void init() {
    }
}
