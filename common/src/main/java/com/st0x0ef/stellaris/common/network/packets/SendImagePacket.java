package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.GameInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.st0x0ef.stellaris.Stellaris.LOG;

public class SendImagePacket implements CustomPacketPayload {

    public final byte[] image;

    public static final StreamCodec<RegistryFriendlyByteBuf, SendImagePacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull SendImagePacket decode(RegistryFriendlyByteBuf buf) {
            return new SendImagePacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SendImagePacket packet) {
            buf.writeByteArray(packet.image);
        }
    };

    public SendImagePacket(byte[] image) {
        this.image = image;
    }

    public SendImagePacket(FriendlyByteBuf buf)  {
        this.image = buf.readByteArray();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return NetworkRegistry.SEND_IMAGE_ID;
    }

    public static void handle(SendImagePacket packet, NetworkManager.PacketContext context) {
        if (packet.image.length > 16 * 1024 * 1024 /*64mb*/) //TODO make size(64) configurable server-side
            return;

        try {
            ImageIO.read(new ByteArrayInputStream(packet.image)).toString();
        } catch (Exception e) {
            LOG.error("Uploaded file was not an image. Player UUID is: {}", context.getPlayer().getUUID()); // Failsafe if someone tampers with the methods
            return;
        }

        try {
            MinecraftServer server = GameInstance.getServer();
            if (server != null)
                FileUtils.writeByteArrayToFile(server.getServerDirectory()
                                .resolve("flags/" + context.getPlayer().getUUID()  + ".png").toFile(),
                        packet.image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
