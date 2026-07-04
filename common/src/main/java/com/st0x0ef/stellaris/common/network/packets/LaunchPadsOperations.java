package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.machines.AntennaBlockEntity;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadUtils;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
        String playerName = context.getPlayer().getName().getString();

        switch (packet.action) {
            case "add" -> {
                if (!launchPad.owner().equals(playerName)) return;
                LaunchPadLauncher.addLaunchPad(launchPad, context.getPlayer().getServer());
            }
            case "modify" -> {
                LaunchPad existing = LaunchPadUtils.getPadById(launchPad.id());
                if (existing == null || !existing.owner().equals(playerName)) return;
                LaunchPad toPersist = new LaunchPad(existing.id(), existing.position(), existing.dimension(),
                        launchPad.name(), launchPad.isPublic(), existing.owner(), launchPad.whitelist());
                LaunchPadLauncher.modifyLaunchPad(toPersist, context.getPlayer().getServer());
            }
            case "remove" -> {
                LaunchPad existing = LaunchPadUtils.getPadById(launchPad.id());
                if (existing == null || !existing.owner().equals(playerName)) return;
                LaunchPadLauncher.removeLaunchpad(launchPad.id() ,context.getPlayer().getServer());
            }
            case "setLaunchPad" -> {
                BlockPos pos = Utils.getBlockPosFromVector3i(launchPad.position());
                if (context.getPlayer().distanceToSqr(Vec3.atCenterOf(pos)) > 64.0) return;

                if (context.getPlayer().level().getBlockEntity(pos) instanceof AntennaBlockEntity blockEntity) {
                    LaunchPad target = LaunchPadUtils.getPadById(launchPad.id());
                    if (target == null || !target.owner().equals(playerName)) return;

                    if (blockEntity.launchPadId != -1) {
                        LaunchPad current = LaunchPadUtils.getPadById(blockEntity.launchPadId);
                        if (current != null && !current.owner().equals(playerName)) return;
                    }

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