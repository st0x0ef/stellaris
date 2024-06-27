package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.List;

public class SyncPlanetsDatapack {
    private final List<Planet> planets;

    public SyncPlanetsDatapack(RegistryFriendlyByteBuf buffer) {
        this.planets = Planet.readFromBuffer(buffer);
    }

    public SyncPlanetsDatapack(List<Planet> planets) {
        this.planets = planets;
    }

    public static RegistryFriendlyByteBuf encode(SyncPlanetsDatapack message, RegistryFriendlyByteBuf buffer) {
        return Planet.toBuffer(message.planets, buffer);
    }

    public static void apply(RegistryFriendlyByteBuf buffer, NetworkManager.PacketContext context) {
        StellarisData.addPlanets(new SyncPlanetsDatapack(buffer).planets);
    }
}
