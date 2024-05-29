package com.st0x0ef.stellaris.client.events;

import com.st0x0ef.stellaris.client.registries.KeyMappings;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.network.packets.KeyHandler;
import dev.architectury.event.events.client.ClientTickEvent;

public class ClientEvents {

    public static void registerEvents() {
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (KeyMappings.ROCKET_START.consumeClick()) {
                NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("rocket_start", true));
            }
            while (KeyMappings.FREEZE_PLANET_MENU.consumeClick()) {
                NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("freeze_planet_menu", true));
            }
            while (minecraft.options.keyJump.consumeClick()) {
                NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("key_jump", true));
            }

            if (minecraft.getCurrentServer() != null) {
                while (minecraft.options.keyJump.isDefault()) {
                    NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("key_jump", false));
                }
            }
        });
    }

}
