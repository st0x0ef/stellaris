package com.st0x0ef.stellaris.client.events;

import com.st0x0ef.stellaris.client.registries.KeyMappings;
import com.st0x0ef.stellaris.common.network.packets.KeyHandler;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.event.events.client.ClientTickEvent;

public class ClientEvents {

    public static void registerEvents() {
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (KeyMappings.ROCKET_START.consumeClick()) {
                NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("rocket_start", true));
            }
            while (minecraft.options.keyJump.consumeClick()) {
                NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("key_jump", true));
            }

            if (minecraft.getCurrentServer() != null) {
                while (!minecraft.options.keyJump.consumeClick()) {
                    NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("key_jump", false));
                }
            }



        });
    }

}
