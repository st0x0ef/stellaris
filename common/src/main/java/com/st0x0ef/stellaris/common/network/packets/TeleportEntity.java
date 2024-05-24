package com.st0x0ef.stellaris.common.network.packets;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.registries.KeyMappings;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;

import java.util.function.Supplier;

public class TeleportEntity {

    public final ResourceKey<Level> dimension;
    public final boolean orbit;

    public TeleportEntity(ResourceKey<Level> dimension, boolean orbit) {
        this.dimension = dimension;

        this.orbit = orbit;
    }

    public TeleportEntity(FriendlyByteBuf buffer) {
        this.dimension = buffer.readResourceKey(Registries.DIMENSION);
        this.orbit = buffer.readBoolean();
    }

    public static TeleportEntity decode(FriendlyByteBuf buffer) {
        return new TeleportEntity(buffer);
    }

    public static void encode(TeleportEntity message, FriendlyByteBuf buffer) {
        buffer.writeResourceKey(message.dimension);
        buffer.writeBoolean(message.orbit);
    }

    public static void apply(TeleportEntity message, Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();

        Player player = context.getPlayer();
        Planet planet = StellarisData.getPlanet(message.dimension);
        if(planet != null) {
            Stellaris.LOG.error("Planet exist: " + planet.dimension());
            Utils.changeDimension(player, planet, false);
        } else {
            Stellaris.LOG.error("Planet is null");
        }
    }



}
