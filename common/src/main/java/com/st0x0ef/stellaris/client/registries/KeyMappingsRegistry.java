package com.st0x0ef.stellaris.client.registries;

import com.mojang.blaze3d.platform.InputConstants;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.network.packets.KeyHandlerPacket;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;

public class KeyMappingsRegistry {
    public static void register() {
        KeyMapping ROCKET_START = new KeyMapping("key." + Stellaris.MODID + ".rocket_start", InputConstants.Type.KEYSYM, InputConstants.KEY_SPACE, "category." + Stellaris.MODID + ".default");
        KeyMapping FREEZE_PLANET_MENU = new KeyMapping("key." + Stellaris.MODID + ".freeze_planet_menu", InputConstants.Type.KEYSYM, InputConstants.KEY_X, "category." + Stellaris.MODID + ".default");
        KeyMapping CHANGE_JETSUIT_MODE = new KeyMapping("key." + Stellaris.MODID + ".jetsuit_mode", InputConstants.Type.KEYSYM, InputConstants.KEY_V, "category." + Stellaris.MODID + ".default");

        KeyMappingRegistry.register(ROCKET_START);
        KeyMappingRegistry.register(FREEZE_PLANET_MENU);
        KeyMappingRegistry.register(CHANGE_JETSUIT_MODE);

        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (ROCKET_START.consumeClick()) {

                NetworkManager.sendToServer(new KeyHandlerPacket(
                        "rocket_start", true
                ));
            }

            while (FREEZE_PLANET_MENU.consumeClick()) {

                NetworkManager.sendToServer(new KeyHandlerPacket(
                        "freeze_planet_menu", true
                ));


            }

            while (CHANGE_JETSUIT_MODE.consumeClick()) {
                NetworkManager.sendToServer(new KeyHandlerPacket(
                        "switch_jet_suit_mode", true
                ));
            }
        });
    }
}
