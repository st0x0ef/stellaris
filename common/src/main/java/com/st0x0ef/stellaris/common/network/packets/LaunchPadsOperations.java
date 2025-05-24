package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.machines.LaunchPadCreatorBlockEntity;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class LaunchPadsOperations implements CustomPacketPayload {

    private final LaunchPad launchPad;
    private final String action;

    public static final StreamCodec<RegistryFriendlyByteBuf, LaunchPadsOperations> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull LaunchPadsOperations decode(RegistryFriendlyByteBuf buf) {
            return new LaunchPadsOperations(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, LaunchPadsOperations packet) {
            LaunchPad.toBuffer(packet.launchPad, buf);
            buf.writeUtf(packet.action);
        }
    };

    public LaunchPadsOperations(RegistryFriendlyByteBuf buffer) {
        this.launchPad = LaunchPad.readFromBuffer( buffer);
        this.action = buffer.readUtf();
    }

    public LaunchPadsOperations(LaunchPad planets, String action) {
        this.launchPad = planets;
        this.action = action;
    }


    public static void handle(LaunchPadsOperations packet, NetworkManager.PacketContext context) {
        //On the Server
        LaunchPad launchPad = packet.launchPad;
        Level level = context.getPlayer().level();
        switch (packet.action) {
            case "add" -> {
                LaunchPadLauncher.addLaunchPad(launchPad, context.getPlayer().getServer());
            }
            case "remove" -> {
                LaunchPadLauncher.removeLaunchpad(launchPad.id() ,context.getPlayer().getServer());
            }
            case "setLaunchPad" -> {
                if(context.getPlayer().level().getBlockEntity(Utils.getBlockPosFromVector3i(launchPad.position())) instanceof LaunchPadCreatorBlockEntity blockEntity) {
                    blockEntity.launchPadId = launchPad.id();
                    blockEntity.setChanged();
                }
            }
        }

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkRegistry.LAUNCH_PADS_OPERATION;
    }
}