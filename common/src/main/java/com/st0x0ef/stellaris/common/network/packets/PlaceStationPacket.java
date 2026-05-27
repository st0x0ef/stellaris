package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.recipes.SpaceStationRecipe;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.launchpads.LaunchPadLauncher;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PlaceStationPacket implements CustomPacketPayload {

    public final ResourceLocation dimension;
    public final SpaceStationRecipe recipe;
    public final LaunchPad pad;

    public static final StreamCodec<RegistryFriendlyByteBuf, PlaceStationPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull PlaceStationPacket decode(RegistryFriendlyByteBuf buf) {
            return new PlaceStationPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PlaceStationPacket packet) {
            buf.writeResourceLocation(packet.dimension);
            SpaceStationRecipe.toBuffer(packet.recipe, buf);
            LaunchPad.toBuffer(packet.pad, buf);
        }
    };

    public PlaceStationPacket(ResourceLocation dimension, SpaceStationRecipe recipe, LaunchPad pad) {
        this.dimension = dimension;
        this.recipe = recipe;
        this.pad = pad;
    }

    public PlaceStationPacket(RegistryFriendlyByteBuf buffer) {
        this.dimension = buffer.readResourceLocation();
        this.recipe = SpaceStationRecipe.readFromBuffer(buffer);
        this.pad = LaunchPad.readFromBuffer(buffer);
    }

    public static void handle(PlaceStationPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
            Planet planet = PlanetUtil.getPlanet(packet.dimension);

            if (planet == null) {
                Stellaris.LOG.warn("Received station placement packet for unknown planet {}", packet.dimension);
                return;
            }

            MinecraftServer server = player.getServer();
            if (server == null) {
                Stellaris.LOG.warn("Unable to place space station: server is null for player {}", player.getName().getString());
                return;
            }

            ServerLevel level = server.getLevel(ResourceKey.create(Registries.DIMENSION, planet.dimension()));
            if (level == null) {
                Stellaris.LOG.warn("Unable to resolve destination level {} for station placement", planet.dimension());
                return;
            }

            Vec3 stationPosition = Utils.placeSpaceStation(player, level, packet.recipe, packet.pad);
            if (stationPosition == null) {
                player.sendSystemMessage(Component.literal("Space station placement failed. Please try again."));
                return;
            }

            if (!player.isCreative() && !player.isSpectator()) {
                packet.recipe.removeMaterials(player);
            }

            LaunchPad newPad = new LaunchPad(
                    packet.pad.id(),
                    stationPosition,
                    packet.pad.dimension(),
                    packet.pad.name(),
                    packet.pad.isPublic(),
                    packet.pad.owner(),
                    packet.pad.whitelist()
            );

            LaunchPadLauncher.addLaunchPad(newPad, server);
            TeleportEntityToPlanetPacket.teleportToPlanet(player, packet.dimension, stationPosition);
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return NetworkRegistry.PLACE_STATION_ID;
    }
}