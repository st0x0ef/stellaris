package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.events.custom.PlanetSelectionServerEvents;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadUtils;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.event.EventResult;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TeleportEntityToPlanetPacket implements CustomPacketPayload {

    public final ResourceLocation dimension;
    public final Vec3 coords;
    public final int launchPadId;

    public static final StreamCodec<RegistryFriendlyByteBuf, TeleportEntityToPlanetPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull TeleportEntityToPlanetPacket decode(RegistryFriendlyByteBuf buf) {
            return new TeleportEntityToPlanetPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, TeleportEntityToPlanetPacket packet) {
            buf.writeResourceLocation(packet.dimension);
            buf.writeVec3(packet.coords);
            buf.writeInt(packet.launchPadId);
        }
    };

    public TeleportEntityToPlanetPacket(ResourceLocation dimension, Vec3 coords) {
        this(dimension, coords, -1);
    }

    public TeleportEntityToPlanetPacket(ResourceLocation dimension, Vec3 coords, int launchPadId) {
        this.dimension = dimension;
        this.coords = coords;
        this.launchPadId = launchPadId;
    }

    public TeleportEntityToPlanetPacket(RegistryFriendlyByteBuf buffer) {
        this.dimension = buffer.readResourceLocation();
        this.coords = buffer.readVec3();
        this.launchPadId = buffer.readInt();
    }

    public static void handle(TeleportEntityToPlanetPacket packet, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();

        Vec3 coords = packet.coords;
        ResourceLocation dimension = packet.dimension;

        if (packet.launchPadId != -1) {
            LaunchPad pad = LaunchPadUtils.getPadById(packet.launchPadId);
            if (pad == null || !LaunchPadUtils.canPlayerJoinLaunchPad(pad, player)) {
                Stellaris.LOG.warn("{} tried to launch to launchpad {} without access", player.getName().getString(), packet.launchPadId);
                return;
            }
            coords = pad.position();
            dimension = pad.dimension().location();
        }

        Planet planet = PlanetUtil.getPlanet(dimension);
        Entity rocket = player.getVehicle();

        if(PlanetSelectionServerEvents.LAUNCH_BUTTON.invoker().launchButton(player, planet, rocket, context) != EventResult.interruptTrue()) {
            teleportToPlanet(player, dimension, coords);
        }

    }

    public static void teleportToPlanet(Player player, ResourceLocation dimension, Vec3 coords) {
        Planet planet = PlanetUtil.getPlanet(dimension);
        Entity rocket = player.getVehicle();

        if(planet != null ) {
            if(rocket == null) {
                Utils.changeDimension(player, planet, coords);
                return;
            }

            if(rocket.getPassengers().size() == 1) {
                List<Entity> passengers = rocket.getPassengers();
                Utils.changeDimension((Player) passengers.getFirst(), planet);
                player.stellaris$setPlanetMenuOpen(false, (Player) passengers.getFirst(), true);
            } else {
                Utils.changeDimensionForPlayers(rocket.getPassengers(), planet, coords, true);
            }

        } else {
            Stellaris.LOG.error("Planet is null");
        }

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NetworkRegistry.TELEPORT_ENTITY_ID;
    }
}