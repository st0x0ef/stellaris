package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class SyncPlanetMenuState implements CustomPacketPayload {

    final boolean open;

    public static final Type<SyncPlanetMenuState> TYPE = new Type<>(ResourceLocationUtils.id("sync_planet_menu_state"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPlanetMenuState> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public @NotNull SyncPlanetMenuState decode(RegistryFriendlyByteBuf buf) {
            return new SyncPlanetMenuState(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncPlanetMenuState packet) {
            buf.writeBoolean(packet.open);
        }
    };


    public SyncPlanetMenuState(RegistryFriendlyByteBuf buffer) {
        this(buffer.readBoolean());
    }

    public SyncPlanetMenuState(boolean open) {
        this.open = open;
    }


    public static void handle(SyncPlanetMenuState packet, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        player.stellaris$setPlanetMenuOpen(packet.open, player, false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
