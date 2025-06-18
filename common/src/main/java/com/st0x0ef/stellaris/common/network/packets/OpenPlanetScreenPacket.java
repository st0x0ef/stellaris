package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record OpenPlanetScreenPacket(String galaxyId) implements CustomPacketPayload {
    public static final Type<OpenPlanetScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("stellaris", "open_planetscreen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenPlanetScreenPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeUtf(packet.galaxyId),
                    buf -> new OpenPlanetScreenPacket(buf.readUtf())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenPlanetScreenPacket pkt, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            String galaxyId = pkt.galaxyId();
            PlanetUtil.openPlanetSelectionMenu(serverPlayer, true, galaxyId);
        }
    }
}
