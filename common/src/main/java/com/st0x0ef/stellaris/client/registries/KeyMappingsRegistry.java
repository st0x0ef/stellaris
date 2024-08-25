package com.st0x0ef.stellaris.client.registries;

import com.mojang.blaze3d.platform.InputConstants;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.network.packets.KeyHandlerPacket;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class KeyMappingsRegistry {
    public static String category = "category." + Stellaris.MODID + ".default";

    public static KeyMapping ROCKET_START = new KeyMapping("key." + Stellaris.MODID + ".rocket_start", InputConstants.KEY_SPACE, category);
    public static KeyMapping FREEZE_PLANET_MENU = new KeyMapping("key." + Stellaris.MODID + ".freeze_planet_menu", InputConstants.KEY_X, category);
    public static KeyMapping CHANGE_JETSUIT_MODE = new KeyMapping("key." + Stellaris.MODID + ".jetsuit_mode", InputConstants.KEY_V, category);

    public static void clientTick(Minecraft minecraft) {
        while (ROCKET_START.consumeClick()) {
            NetworkManager.sendToServer(new KeyHandlerPacket("rocket_start", true));
        }

        while (FREEZE_PLANET_MENU.consumeClick()) {
            NetworkManager.sendToServer(new KeyHandlerPacket("freeze_planet_menu", true));
        }

        while (CHANGE_JETSUIT_MODE.consumeClick()) {
            NetworkManager.sendToServer(new KeyHandlerPacket("switch_jet_suit_mode", true));
        }
    }
}
