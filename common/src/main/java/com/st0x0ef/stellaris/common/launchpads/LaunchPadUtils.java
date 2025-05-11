package com.st0x0ef.stellaris.common.launchpads;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LaunchPadUtils {

    public static boolean launchPadHasSameName(LaunchPad launchPad) {

        for (LaunchPad lp : LaunchPadLauncher.LAUNCH_PADS.launchPads()) {
            if (lp.name().equals(launchPad.name())) {
                return true;
            }
        }
        return false;
    }

    public static boolean launchPadExist(LaunchPad launchPad) {

        for (LaunchPad lp : LaunchPadLauncher.LAUNCH_PADS.launchPads()) {
            if (lp.name().equals(launchPad.name()) && lp.dimension().equals(launchPad.dimension())) {
                return true;
            }
        }
        return false;
    }

    public static boolean launchPadExistInDimension(LaunchPad launchPad, ResourceKey<Level> dimension) {

        for (LaunchPad lp : LaunchPadLauncher.LAUNCH_PADS.launchPads()) {
            if (lp.name().equals(launchPad.name()) && lp.dimension().equals(dimension)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canPlayerJoinLaunchPad(LaunchPad launchPad, Player player) {

        if(launchPad.isPublic()) {
            return true;
        }
        if(launchPad.whitelist().contains(player.getName().getString())) {
            return true;
        }

        return launchPad.owner().equals(player.getName().getString());
    }
}
