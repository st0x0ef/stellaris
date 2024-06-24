package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.List;

public class SyncPlanetsDatapack extends BaseS2CMessage {
    private final List<Planet> planets;

    public SyncPlanetsDatapack(RegistryFriendlyByteBuf buffer) {
        this.planets = Planet.readFromBuffer(buffer);
    }

    public SyncPlanetsDatapack(List<Planet> planets) {
        this.planets = planets;
    }

    @Override
    public MessageType getType() {
        return NetworkRegistry.SYNC_PLANETS_DATAPACK;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        Planet.toBuffer(this.planets, buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        StellarisData.addPlanets(this.planets);
    }
}
