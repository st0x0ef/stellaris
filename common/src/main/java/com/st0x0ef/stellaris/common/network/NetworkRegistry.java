package com.st0x0ef.stellaris.common.network;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.network.packets.*;
import dev.architectury.impl.NetworkAggregator;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.transformers.PacketSink;
import dev.architectury.networking.transformers.SplitPacketTransformer;
import dev.architectury.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;

public class NetworkRegistry {
    public static final ResourceLocation KEY_HANDLER_ID = new ResourceLocation(Stellaris.MODID, "key_handler");
    public static final ResourceLocation SYNC_PLANET_DATAPACK_ID = new ResourceLocation(Stellaris.MODID, "sync_planet_datapack");
    public static final ResourceLocation TELEPORT_ENTITY_ID = new ResourceLocation(Stellaris.MODID, "teleport_entity");
    public static final ResourceLocation SYNC_ROCKET_COMPONENT_ID = new ResourceLocation(Stellaris.MODID, "sync_rocket_component");
    public static final ResourceLocation SYNC_FLUID_TANKS_ID = new ResourceLocation(Stellaris.MODID, "sync_fluid_tanks");

    public static void registerC2S() {
        NetworkAggregator.registerReceiver(NetworkManager.Side.C2S, KEY_HANDLER_ID, Collections.singletonList(new SplitPacketTransformer()), KeyHandler::apply);
        NetworkAggregator.registerReceiver(NetworkManager.Side.C2S, TELEPORT_ENTITY_ID, Collections.singletonList(new SplitPacketTransformer()), TeleportEntityToPlanet::apply);
    }

    public static void registerS2C() {
        NetworkAggregator.registerReceiver(NetworkManager.Side.S2C, SYNC_PLANET_DATAPACK_ID, Collections.emptyList(), SyncPlanetsDatapack::apply);
        NetworkAggregator.registerReceiver(NetworkManager.Side.S2C, SYNC_ROCKET_COMPONENT_ID, Collections.emptyList(), SyncRocketComponent::apply);
        NetworkAggregator.registerReceiver(NetworkManager.Side.S2C, SYNC_FLUID_TANKS_ID, Collections.emptyList(), SyncWidgetsTanks::apply);
    }


    public static void sendToPlayer(ServerPlayer player, ResourceLocation packet_id, RegistryFriendlyByteBuf buffer) {
        NetworkAggregator.collectPackets(PacketSink.ofPlayer(player), NetworkManager.Side.S2C, packet_id, buffer);
    }

    public static void sendToServer(ResourceLocation packet_id, RegistryFriendlyByteBuf buffer) {
        NetworkAggregator.collectPackets(PacketSink.client(), NetworkManager.Side.C2S, packet_id, buffer);
    }
}
