package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record OpenMilkyWayMenuPacket() implements CustomPacketPayload {
    public static final Type<OpenMilkyWayMenuPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("stellaris", "open_milky_way"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenMilkyWayMenuPacket> STREAM_CODEC =
            StreamCodec.unit(new OpenMilkyWayMenuPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenMilkyWayMenuPacket pkt, NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            PlanetUtil.openMilkyWayMenu(serverPlayer);
        }
    }
}
