package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SyncPlanetsDatapackPacket implements CustomPacketPayload {
    private final List<Planet> planets;


    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlanetsDatapackPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull SyncPlanetsDatapackPacket decode(RegistryFriendlyByteBuf buf) {
            return new SyncPlanetsDatapackPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncPlanetsDatapackPacket packet) {
            Planet.toBuffer(packet.planets, buf);
        }
    };

    public SyncPlanetsDatapackPacket(RegistryFriendlyByteBuf buffer) {
        this.planets = Planet.readFromBuffer(buffer);
    }

    public SyncPlanetsDatapackPacket(List<Planet> planets) {
        this.planets = planets;
    }


    public static void handle(SyncPlanetsDatapackPacket packet, NetworkManager.PacketContext context) {
        StellarisData.addPlanets(packet.planets);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkRegistry.SYNC_PLANETS_DATAPACK;
    }
}
