package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.config.CommonConfig;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SyncConfigPacket implements CustomPacketPayload {

    public static final Type<SyncConfigPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "sync_config"));

    private final String config;

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncConfigPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull SyncConfigPacket decode(RegistryFriendlyByteBuf buf) {
            return new SyncConfigPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncConfigPacket packet) {
            buf.writeUtf(packet.config);
        }
    };

    public SyncConfigPacket(RegistryFriendlyByteBuf buffer) {
        this.config = buffer.readUtf();
    }

    public SyncConfigPacket(CommonConfig config) {
        this.config = Stellaris.GSON.toJson(config, CommonConfig.class);
    }

    public static void handle(SyncConfigPacket packet, NetworkManager.PacketContext context) {
        CommonConfig config;

        try {
            config = Stellaris.GSON.fromJson(packet.config, CommonConfig.class);

        } catch (Exception e) {
            Stellaris.LOG.error("Failed to parse config packet: {}", e.getMessage());
            config = new CommonConfig();
        }
        Stellaris.CONFIG = config;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
