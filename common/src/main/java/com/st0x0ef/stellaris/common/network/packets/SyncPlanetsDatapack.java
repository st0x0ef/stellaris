package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class SyncPlanetsDatapack {

    private final ResourceKey resourceKey;
    private final Planet planet;

    public SyncPlanetsDatapack(FriendlyByteBuf buffer) {
        this(buffer.readResourceKey(Registries.DIMENSION), Planet.fromNetwork(buffer));
    }
    public SyncPlanetsDatapack(ResourceKey<Level> resourceKey, Planet planet) {
        this.planet = planet;
        this.resourceKey = resourceKey;
    }


    public static SyncPlanetsDatapack decode(FriendlyByteBuf buffer) {
        return new SyncPlanetsDatapack(buffer);
    }

    public static void encode(SyncPlanetsDatapack message, FriendlyByteBuf buffer) {
        buffer.writeResourceKey(message.resourceKey);
        message.planet.toNetwork(buffer);
    }

    public static void apply(SyncPlanetsDatapack message, Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();
        contextSupplier.get().queue(() -> {
            StellarisData.addPlanet(message.resourceKey, message.planet);
        });

    }


}
