package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class TeleportEntityToPlanetPacket extends BaseC2SMessage {
    public final ResourceLocation dimension;

    public TeleportEntityToPlanetPacket(ResourceLocation dimension) {
        this.dimension = dimension;
    }

    public TeleportEntityToPlanetPacket(FriendlyByteBuf buffer) {
        this.dimension = buffer.readResourceLocation();
    }


    @Override
    public MessageType getType() {
        return NetworkRegistry.TELEPORT_ENTITY_ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(dimension);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        Planet planet = PlanetUtil.getPlanet(dimension);
        if(planet != null) {
            Utils.changeDimension(player, planet);
        } else {
            Stellaris.LOG.error("Planet is null");
        }
    }
}