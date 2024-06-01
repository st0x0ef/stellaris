package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.SerializationUtils;

public class TeleportEntity {

    public final ResourceKey<Level> dimension;
    public final boolean orbit;

    public TeleportEntity(ResourceKey<Level> dimension, boolean orbit) {
        this.dimension = dimension;
        this.orbit = orbit;
    }

    public TeleportEntity(RegistryFriendlyByteBuf buffer) {
        this.dimension = buffer.readResourceKey(Registries.DIMENSION);
        this.orbit = buffer.readBoolean();
    }

    public static RegistryFriendlyByteBuf encode(TeleportEntity message, RegistryFriendlyByteBuf buffer) {
        buffer.writeResourceKey(message.dimension);
        buffer.writeBoolean(message.orbit);
        return buffer;
    }

    public static void apply(RegistryFriendlyByteBuf buffer, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        Planet planet = PlanetUtil.getPlanet(new TeleportEntity(buffer).dimension);
        if(planet != null) {
            Utils.changeDimension(player, planet, false);
        } else {
            Stellaris.LOG.error("Planet is null");
        }
    }
}