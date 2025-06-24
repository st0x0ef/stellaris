package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class SyncLaunchPads implements CustomPacketPayload {

    private final LaunchPad.LaunchPadContainer container;

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncLaunchPads> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull SyncLaunchPads decode(RegistryFriendlyByteBuf buf) {
            return new SyncLaunchPads(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncLaunchPads packet) {
            LaunchPad.LaunchPadContainer.toBuffer(packet.container, buf);
        }
    };

    public SyncLaunchPads(RegistryFriendlyByteBuf buffer) {
        this.container = LaunchPad.LaunchPadContainer.readFromBuffer( buffer);
    }

    public SyncLaunchPads(LaunchPad.LaunchPadContainer planets) {
        this.container = planets;
    }


    public static void handle(SyncLaunchPads packet, NetworkManager.PacketContext context) {
        //On the client
        LaunchPadLauncher.LAUNCH_PADS = packet.container;
        PlanetSelectionScreen.LAUNCH_PADS = packet.container;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkRegistry.SYNC_LAUNCH_PADS;
    }
}