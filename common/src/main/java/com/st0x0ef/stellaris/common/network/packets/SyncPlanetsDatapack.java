package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Map;

public class SyncPlanetsDatapack {
    private final Map<ResourceLocation, Planet> planetData;

    public SyncPlanetsDatapack(RegistryFriendlyByteBuf buffer) {
        this.planetData = SerializationUtils.deserialize(buffer.readByteArray());
    }

    public SyncPlanetsDatapack(Map<ResourceLocation, Planet> planetData) {
        this.planetData = planetData;
    }

    public static RegistryFriendlyByteBuf encode(SyncPlanetsDatapack message, RegistryFriendlyByteBuf buffer) {
        return (RegistryFriendlyByteBuf) buffer.writeByteArray(SerializationUtils.serialize((Serializable) message.planetData));
    }

    public static void apply(RegistryFriendlyByteBuf buffer, NetworkManager.PacketContext context) {
        SyncPlanetsDatapack syncPlanetsDatapack = new SyncPlanetsDatapack(buffer);
        context.queue(() -> {
            syncPlanetsDatapack.planetData.forEach(StellarisData::addPlanet);
        });
    }
}
