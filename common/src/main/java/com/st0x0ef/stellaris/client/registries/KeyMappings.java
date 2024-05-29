package com.st0x0ef.stellaris.client.registries;

import com.mojang.blaze3d.platform.InputConstants;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.entities.RocketEntity;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;

public class KeyMappings {

    public static final KeyMapping ROCKET_START = new KeyMapping(
            "key." + Stellaris.MODID + ".rocket_start", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputConstants.KEY_SPACE, // The default keycode
            "category." + Stellaris.MODID + ".default" // The category translation key used to categorize in the Controls screen
    );

    public static final KeyMapping FREEZE_PLANET_MENU = new KeyMapping(
            "key." + Stellaris.MODID + ".freeze_planet_menu", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputConstants.KEY_X, // The default keycode
            "category." + Stellaris.MODID + ".default" // The category translation key used to categorize in the Controls screen
    );


    public static void startRocket(Player player) {
        if(player.getVehicle() instanceof RocketEntity rocketEntity) {
            if (rocketEntity.getFuel() >= RocketEntity.MAX_FUEL) {
                rocketEntity.startRocket();
            }
        }
    }
}
