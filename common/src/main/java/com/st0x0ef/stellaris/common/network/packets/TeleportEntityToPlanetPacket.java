package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportEntityToPlanetPacket implements CustomPacketPayload {

    public final ResourceLocation dimension;

    public static final StreamCodec<RegistryFriendlyByteBuf, TeleportEntityToPlanetPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull TeleportEntityToPlanetPacket decode(RegistryFriendlyByteBuf buf) {
            return new TeleportEntityToPlanetPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, TeleportEntityToPlanetPacket packet) {
            buf.writeResourceLocation(packet.dimension);
        }
    };



    public TeleportEntityToPlanetPacket(ResourceLocation dimension) {
        this.dimension = dimension;
    }

    public TeleportEntityToPlanetPacket(RegistryFriendlyByteBuf buffer) {
        this.dimension = buffer.readResourceLocation();
    }


    public static void handle(TeleportEntityToPlanetPacket packet, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        Planet planet = PlanetUtil.getPlanet(packet.dimension);
        if(planet != null) {
            Utils.changeDimension(player, planet);
        } else {
            Stellaris.LOG.error("Planet is null");
        }

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkRegistry.TELEPORT_ENTITY_ID;
    }
}