package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class SyncPlanetsDatapack {

    private final ResourceKey<Level> resourceKey;
    private final Planet planet;

    public SyncPlanetsDatapack(RegistryFriendlyByteBuf buffer) {
        this(buffer.readResourceKey(Registries.DIMENSION), Planet.fromNetwork(buffer));
    }
    public SyncPlanetsDatapack(ResourceKey<Level> resourceKey, Planet planet) {
        this.planet = planet;
        this.resourceKey = resourceKey;
    }

    public static RegistryFriendlyByteBuf encode(SyncPlanetsDatapack message, RegistryFriendlyByteBuf buffer) {
        buffer.writeResourceKey(message.resourceKey);
        return message.planet.toNetwork(buffer);
    }

    public static void apply(RegistryFriendlyByteBuf buffer, NetworkManager.PacketContext context) {
        SyncPlanetsDatapack syncPlanetsDatapack = new SyncPlanetsDatapack(buffer);
        context.queue(() -> {
            StellarisData.addPlanet(syncPlanetsDatapack.resourceKey, syncPlanetsDatapack.planet);
        });
    }


}
