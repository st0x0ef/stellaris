package com.st0x0ef.stellaris.common.network;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;


public class NetworkRegistry {

    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation(Stellaris.MODID, "networking_channel"));

    public static void register(){
        CHANNEL.register(KeyHandler.class, KeyHandler::encode, KeyHandler::new, KeyHandler::apply);

    }

}
