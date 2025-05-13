package com.st0x0ef.stellaris.common.network;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.network.packets.*;
import dev.architectury.impl.NetworkAggregator;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public interface NetworkRegistry {
    CustomPacketPayload.Type<KeyHandlerPacket> KEY_HANDLER_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "key_handler"));
    CustomPacketPayload.Type<TeleportEntityToPlanetPacket> TELEPORT_ENTITY_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "teleport_entity"));
    CustomPacketPayload.Type<OpenTabletEntryPacket> TABLET_OPEN_HANDLER_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "tablet_open_handler"));
    CustomPacketPayload.Type<PlaceStationPacket> PLACE_STATION_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "place_station"));

    CustomPacketPayload.Type<SyncPlanetsDatapackPacket> SYNC_PLANETS_DATAPACK = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "sync_planet_datapack"));
    CustomPacketPayload.Type<SyncRocketComponentPacket> SYNC_ROCKET_COMPONENT_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "sync_rocket_component"));
    CustomPacketPayload.Type<SyncSpaceStationDatapackPacket> SYNC_SPACE_STATION_DATAPACK = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "sync_space_station_datapack"));
    CustomPacketPayload.Type<SyncRoverComponentPacket> SYNC_ROVER_COMPONENT_ID = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "sync_rover_component"));
    CustomPacketPayload.Type<SyncRoverPacket> SYNC_ROVER_CONTROLS = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "sync_rover_packet"));
    CustomPacketPayload.Type<SyncLaunchPads> SYNC_LAUNCH_PADS = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "sync_launch_pads"));
    CustomPacketPayload.Type<LaunchPadsOperations> LAUNCH_PADS_OPERATION = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "launch_pads_operation"));

    CustomPacketPayload.Type<OpenMilkyWayMenuPacket> OPEN_MILKY_WAY_MENU =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "open_milky_way"));
    CustomPacketPayload.Type<OpenPlanetScreenPacket> OPEN_PLANETSCREEN_MENU =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "open_planetscreen"));


    static void init() {
        registerC2S(KEY_HANDLER_ID, KeyHandlerPacket.STREAM_CODEC, KeyHandlerPacket::handle);
        registerC2S(TELEPORT_ENTITY_ID, TeleportEntityToPlanetPacket.STREAM_CODEC, TeleportEntityToPlanetPacket::handle);
        registerC2S(SYNC_ROVER_CONTROLS, SyncRoverPacket.STREAM_CODEC,SyncRoverPacket::handle);
        registerC2S(TABLET_OPEN_HANDLER_ID, OpenTabletEntryPacket.STREAM_CODEC, OpenTabletEntryPacket::handle);
        registerC2S(PLACE_STATION_ID, PlaceStationPacket.STREAM_CODEC, PlaceStationPacket::handle);

        registerS2C(SYNC_PLANETS_DATAPACK, SyncPlanetsDatapackPacket.STREAM_CODEC, SyncPlanetsDatapackPacket::handle);
        registerS2C(SYNC_ROCKET_COMPONENT_ID, SyncRocketComponentPacket.STREAM_CODEC, SyncRocketComponentPacket::handle);
        registerS2C(SYNC_ROVER_COMPONENT_ID, SyncRoverComponentPacket.STREAM_CODEC, SyncRoverComponentPacket::handle);
        registerS2C(SyncEnergyPacket.TYPE, SyncEnergyPacket.STREAM_CODEC, SyncEnergyPacket::handle);
        registerS2C(SyncEnergyPacketWithoutDirection.TYPE, SyncEnergyPacketWithoutDirection.STREAM_CODEC, SyncEnergyPacketWithoutDirection::handle);
        registerS2C(SyncFluidPacket.TYPE, SyncFluidPacket.STREAM_CODEC, SyncFluidPacket::handle);
        registerS2C(SyncFluidPacketWithoutDirection.TYPE, SyncFluidPacketWithoutDirection.STREAM_CODEC, SyncFluidPacketWithoutDirection::handle);
        registerS2C(SyncOilLevelPacket.TYPE, SyncOilLevelPacket.STREAM_CODEC, SyncOilLevelPacket::handle);
        registerS2C(SYNC_SPACE_STATION_DATAPACK, SyncSpaceStationDatapackPacket.STREAM_CODEC, SyncSpaceStationDatapackPacket::handle);
        registerS2C(SYNC_LAUNCH_PADS, SyncLaunchPads.STREAM_CODEC, SyncLaunchPads::handle);

        registerC2S(
                OPEN_MILKY_WAY_MENU,
                OpenMilkyWayMenuPacket.STREAM_CODEC,
                OpenMilkyWayMenuPacket::handle
        );
        registerC2S(
                OPEN_PLANETSCREEN_MENU,
                OpenPlanetScreenPacket.STREAM_CODEC,
                OpenPlanetScreenPacket::handle
        );
        registerC2S(LAUNCH_PADS_OPERATION,
                LaunchPadsOperations.STREAM_CODEC,
                LaunchPadsOperations::handle
        );
    }

    static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> packetType, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        if (Platform.getEnvironment().equals(Env.SERVER)) {
            NetworkAggregator.registerS2CType(packetType, codec, List.of());
        } else {
            NetworkAggregator.registerReceiver(NetworkManager.s2c(), packetType, codec, Collections.emptyList(), receiver);
        }
    }

    static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> packetType, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        NetworkAggregator.registerReceiver(NetworkManager.c2s(), packetType, codec, Collections.emptyList(), receiver);
    }
}
