package com.st0x0ef.stellaris.common.launchpads;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

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

    public static void saveLaunchPad(@Nullable LaunchPad launchPad, CompoundTag tag) {
        if(launchPad != null) {
            tag.putBoolean("null" , false);
            tag.putInt("id", launchPad.id());
            return;
        }
        tag.putBoolean("null" , true);
    }

    public static Collection<String> getLaunchPadNames(Player player) {
        return LaunchPadLauncher.LAUNCH_PADS.launchPads().stream()
                .filter(launchPad -> launchPad.owner().equals(player.getName().getString()))
                .map(LaunchPad::name)
                .toList();
    }
    public static Collection<String> getLaunchPadNames() {
        return LaunchPadLauncher.LAUNCH_PADS.launchPads().stream()
                .map( (l) -> "\"" + l.name() + "\"")
                .toList();
    }


    @Nullable
    public static LaunchPad loadLaunchPad(CompoundTag tag) {
        if(tag.getBoolean("null")) {
            return null;
        }
        return getPadById(tag.getInt("id"));
    }

    public static LaunchPad getPadById(int id) {
        for(LaunchPad lp : LaunchPadLauncher.LAUNCH_PADS.launchPads()) {
            if(lp.id() == id) {
                return lp;
            }
        }
        return null;
    }

    public static LaunchPad getPadByNameAndDim(String name, ResourceKey<Level> dimension) {
        for (LaunchPad lp : LaunchPadLauncher.LAUNCH_PADS.launchPads()) {
            if (lp.name().equals(name) && lp.dimension().equals(dimension)) {
                return lp;
            }
        }
        return null;
    }

    public static LaunchPad whitelistPlayer(LaunchPad launchPad, Player player) {
        if (launchPad.whitelist().contains(player.getName().getString())) {
            return launchPad;
        }
        launchPad.whitelist().add(player.getName().getString());
        return launchPad;
    }

    public static LaunchPad getPadByNameAndPlayer(String name, Player player) {
        List<LaunchPad> lps = LaunchPadLauncher.LAUNCH_PADS.launchPads().stream()
                .filter((l) -> l.name().equals(name) && l.owner().equals(player.getName().getString()))
                .toList();

        return lps.isEmpty() ? null : lps.getFirst();
    }
}
