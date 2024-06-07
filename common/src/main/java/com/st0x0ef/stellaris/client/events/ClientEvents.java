package com.st0x0ef.stellaris.client.events;

import com.st0x0ef.stellaris.client.registries.KeyMappings;
import com.st0x0ef.stellaris.common.network.packets.KeyHandler;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.event.events.client.ClientTickEvent;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class ClientEvents {

    public static void registerEvents() {
        ClientTickEvent.CLIENT_LEVEL_POST.register(clientLevel -> {
            while (KeyMappings.ROCKET_START.consumeClick()) {
                RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), clientLevel.registryAccess());
                NetworkRegistry.sendToServer(NetworkRegistry.KEY_HANDLER_ID, KeyHandler.encode(new KeyHandler("rocket_start", true), buffer));
            }

            while (KeyMappings.FREEZE_PLANET_MENU.consumeClick()) {
                RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), clientLevel.registryAccess());
                NetworkRegistry.sendToServer(NetworkRegistry.KEY_HANDLER_ID, KeyHandler.encode(new KeyHandler("freeze_planet_menu", true), buffer));
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
