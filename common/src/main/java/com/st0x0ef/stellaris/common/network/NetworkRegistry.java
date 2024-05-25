package com.st0x0ef.stellaris.common.network;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.network.packets.KeyHandler;
import com.st0x0ef.stellaris.common.network.packets.SyncPlanetsDatapack;
import com.st0x0ef.stellaris.common.network.packets.TeleportEntity;
import dev.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("removal")
public class NetworkRegistry {
    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation(Stellaris.MODID, "networking_channel"));

    public static void register(){
        CHANNEL.register(KeyHandler.class, KeyHandler::encode, KeyHandler::new, KeyHandler::apply);
        CHANNEL.register(SyncPlanetsDatapack.class, SyncPlanetsDatapack::encode, SyncPlanetsDatapack::new, SyncPlanetsDatapack::apply);
        CHANNEL.register(TeleportEntity.class, TeleportEntity::encode, TeleportEntity::new, TeleportEntity::apply);

    }

}
