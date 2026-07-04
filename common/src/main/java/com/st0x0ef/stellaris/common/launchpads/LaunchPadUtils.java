package com.st0x0ef.stellaris.common.launchpads;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

    public static Collection<LaunchPad> getPlayerLaunchPad(Player player) {
        return LaunchPadLauncher.LAUNCH_PADS.launchPads().stream()
                .filter(launchPad -> launchPad.owner().equals(player.getName().getString()))
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
        List<String> newWhitelist = new ArrayList<>(launchPad.whitelist());
        newWhitelist.add(player.getName().getString());
        return new LaunchPad(launchPad.id(), launchPad.position(), launchPad.dimension(),
                launchPad.name(), launchPad.isPublic(), launchPad.owner(), newWhitelist);
    }

    public static LaunchPad.LaunchPadContainer getVisibleLaunchPads(LaunchPad.LaunchPadContainer container, Player player) {
        return new LaunchPad.LaunchPadContainer(container.launchPads().stream()
                .filter(pad -> canPlayerJoinLaunchPad(pad, player))
                .toList());
    }

    public static int getNextLaunchPadId() {
        return LaunchPadLauncher.LAUNCH_PADS.launchPads().stream()
                .mapToInt(LaunchPad::id)
                .max()
                .orElse(-1) + 1;
    }

    public static LaunchPad getPadByNameAndPlayer(String name, Player player) {
        List<LaunchPad> lps = LaunchPadLauncher.LAUNCH_PADS.launchPads().stream()
                .filter((l) -> l.name().equals(name) && l.owner().equals(player.getName().getString()))
                .toList();
        return lps.isEmpty() ? null : lps.getFirst();
    }
}
