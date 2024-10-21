package com.st0x0ef.stellaris.client.registries;

import com.mojang.blaze3d.platform.InputConstants;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.vehicles.LanderEntity;
import com.st0x0ef.stellaris.common.entities.vehicles.RocketEntity;
import com.st0x0ef.stellaris.common.network.packets.KeyHandlerPacket;
import com.st0x0ef.stellaris.common.registry.MenuTypesRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class KeyMappingsRegistry {
    public static String CATEGORY = "category." + Stellaris.MODID + ".default";

    public static KeyMapping ROCKET_START = new KeyMapping("key." + Stellaris.MODID + ".rocket_start", InputConstants.KEY_SPACE, CATEGORY);
    public static KeyMapping FREEZE_PLANET_MENU = new KeyMapping("key." + Stellaris.MODID + ".freeze_planet_menu", InputConstants.KEY_X, CATEGORY);
    public static KeyMapping CHANGE_JETSUIT_MODE = new KeyMapping("key." + Stellaris.MODID + ".jetsuit_mode", InputConstants.KEY_V, CATEGORY);
    public static KeyMapping SLOW_LANDER = new KeyMapping("key." + Stellaris.MODID + ".slow_lander", InputConstants.KEY_SPACE, CATEGORY);

    public static void clientTick(Minecraft minecraft) {
        Player player = minecraft.player;

        if (player == null) {
            return;
        }

        if (player.isPassenger() && player.getVehicle() instanceof RocketEntity) {
            while (ROCKET_START.consumeClick()) {
                NetworkManager.sendToServer(new KeyHandlerPacket("rocket_start", true));
            }
        }

        else if (player.containerMenu == MenuTypesRegistry.PLANET_SELECTION_MENU) {
            while (FREEZE_PLANET_MENU.consumeClick()) {
                NetworkManager.sendToServer(new KeyHandlerPacket("freeze_planet_menu", true));
            }
        }

        else if (Utils.isLivingInJetSuit(player)) {
            while (CHANGE_JETSUIT_MODE.consumeClick()) {
                NetworkManager.sendToServer(new KeyHandlerPacket("switch_jet_suit_mode", true));
            }
        }

        else if(player.isPassenger() && player.getVehicle() instanceof LanderEntity) {
            while (SLOW_LANDER.consumeClick()) {
                NetworkManager.sendToServer(new KeyHandlerPacket("slow_lander", true));
            }
        }
    }
}
