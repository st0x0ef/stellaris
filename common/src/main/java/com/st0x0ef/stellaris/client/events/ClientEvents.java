package com.st0x0ef.stellaris.client.events;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.registries.KeyMappings;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.network.packets.KeyHandler;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public class ClientEvents {

    public static void registerEvents() {
        ClientTickEvent.CLIENT_LEVEL_POST.register(clientLevel -> {
            while (KeyMappings.ROCKET_START.consumeClick()) {
                NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("rocket_start", true));
            }
            while (KeyMappings.FREEZE_PLANET_MENU.consumeClick()) {
                System.out.println("freeze_planet_menu");
                NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("freeze_planet_menu", true));
            }

//            Minecraft minecraft = Minecraft.getInstance();
//            while (!minecraft.options.keyJump.isDown()) {
//                if (!KeyVariables.isHoldingDown(minecraft.player)) {
//                    NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("key_jump", false));
//
//                }
//            }

        });
    }

}
