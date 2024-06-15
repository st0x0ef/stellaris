package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TeleportEntityToPlanet {

    public final ResourceKey<Level> dimension;

    public TeleportEntityToPlanet(ResourceKey<Level> dimension) {
        this.dimension = dimension;
    }

    public TeleportEntityToPlanet(RegistryFriendlyByteBuf buffer) {
        this.dimension = buffer.readResourceKey(Registries.DIMENSION);
    }

    public static RegistryFriendlyByteBuf encode(TeleportEntityToPlanet message, RegistryFriendlyByteBuf buffer) {
        buffer.writeResourceKey(message.dimension);
        return buffer;
    }

    public static void apply(RegistryFriendlyByteBuf buffer, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        Planet planet = PlanetUtil.getPlanet(new TeleportEntityToPlanet(buffer).dimension);
        if(planet != null) {
            Utils.changeDimension(player, planet);
        } else {
            Stellaris.LOG.error("Planet is null");
        }
    }
}