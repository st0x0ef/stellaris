package com.st0x0ef.stellaris.client.events;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.overlays.RocketStartOverlay;
import com.st0x0ef.stellaris.client.registries.KeyMappings;
import com.st0x0ef.stellaris.common.network.packets.KeyHandler;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.world.entity.EntityEvent;

public class ClientEvents {

    public static void registerEvents() {
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (KeyMappings.ROCKET_START.consumeClick()) {
                Stellaris.LOG.info("Rocket Start ");

                NetworkRegistry.CHANNEL.sendToServer(new KeyHandler("rocket_start", true));

            }
        });

        ClientGuiEvent.RENDER_HUD.register(RocketStartOverlay::render);
    }

}
