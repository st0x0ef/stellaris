package com.st0x0ef.stellaris.common.launchpads;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.network.packets.SyncLaunchPads;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;

//The name of this class is funny :)
public class LaunchPadLauncher {

    public static LaunchPad.LaunchPadContainer LAUNCH_PADS;

    public static void loadOrGenerateDefaults(Path worldPath) {
        Path systemsFile = worldPath.resolve("launch-pads.json");

        try {
            BufferedReader reader = Files.newBufferedReader(systemsFile);
            JsonElement jsonElement = GsonHelper.parse(reader);
            JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "launchpads");

            LaunchPadLauncher.LAUNCH_PADS = LaunchPad.LaunchPadContainer.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();


        } catch (Exception e) {

            if (!(e instanceof NoSuchFileException))
                e.printStackTrace();

            LaunchPad.LaunchPadContainer defaults = LaunchPad.LaunchPadContainer.DEFAULT;
            LaunchPadLauncher.LAUNCH_PADS = defaults;

            try {
                File folder = systemsFile.toFile().getParentFile();
                if (!folder.exists())
                    folder.mkdirs();

                JsonElement jsonElement = LaunchPad.LaunchPadContainer.toJson(defaults);
                String launchpadsFile = Stellaris.GSON.toJson(jsonElement);

                BufferedWriter launchpadsWrite = Files.newBufferedWriter(systemsFile);
                launchpadsWrite.write(launchpadsFile);
                launchpadsWrite.close();

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    public static boolean removeLaunchpad(int id, MinecraftServer server)  {
        ArrayList<LaunchPad> launchPads = new ArrayList<>(LaunchPadLauncher.LAUNCH_PADS.launchPads());

        for(LaunchPad launchpad : launchPads) {
            if(launchpad.id() == id) {
                launchPads.remove(launchpad);
                break;
            }
        }

        if(launchPads.equals(LaunchPadLauncher.LAUNCH_PADS.launchPads())) {
            Stellaris.LOG.error("Launchpad {} not found", id);
            return false;
        }

        return writeLaunchpads(launchPads, server);

    }


    public static boolean addLaunchPad(LaunchPad pad, MinecraftServer server)  {

        if(server == null) {
            Stellaris.LOG.error("Server is null");
            return false;
        }
        if (LaunchPadUtils.getPadById(pad.id()) != null) {
            Stellaris.LOG.warn("Rejected add: launchpad id {} already exists", pad.id());
            return false;
        }
        if (LaunchPadUtils.launchPadExistInDimension(pad, pad.dimension())) {
            Stellaris.LOG.warn("Rejected add: a launchpad named {} already exists in {}", pad.name(), pad.dimension());
            return false;
        }
        Stellaris.LOG.error("{} Adding launchpad {}", pad.id(), pad.name());
        ArrayList<LaunchPad> launchPads = new ArrayList<>(LaunchPadLauncher.LAUNCH_PADS.launchPads());
        launchPads.add(pad);

        return writeLaunchpads(launchPads, server);
    }

    public static boolean modifyLaunchPad(LaunchPad pad, MinecraftServer server)  {

        if(server == null) {
            Stellaris.LOG.error("Server is null");
            return false;
        }
        boolean found = false;
        ArrayList<LaunchPad> launchPads = new ArrayList<>();
        for (LaunchPad lpad : LaunchPadLauncher.LAUNCH_PADS.launchPads()) {
            if (pad.id() == lpad.id()) {
                launchPads.add(pad);
                found = true;
            } else {
                launchPads.add(lpad);
            }
        }

        if (!found) {
            Stellaris.LOG.error("Launchpad {} not found", pad.id());
            return false;
        }

        return writeLaunchpads(launchPads, server);
    }

    public static boolean writeLaunchpads(ArrayList<LaunchPad> launchPads, MinecraftServer server) {
        Path launchpath = server.storageSource.getLevelDirectory().path().resolve("launch-pads.json");
        LaunchPad.LaunchPadContainer newContainer = new LaunchPad.LaunchPadContainer(launchPads);

        try {
            JsonElement jsonElement = LaunchPad.LaunchPadContainer.toJson(newContainer);
            String launchpadsFile = Stellaris.GSON.toJson(jsonElement);

            BufferedWriter launchpadsWrite = Files.newBufferedWriter(launchpath);
            launchpadsWrite.write(launchpadsFile);
            launchpadsWrite.close();

        } catch (Exception e) {
            Stellaris.LOG.error("Error writing launchpads to file {}", e.getMessage());
            return false;
        }

        LaunchPadLauncher.LAUNCH_PADS = newContainer;

        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            NetworkManager.sendToPlayer(p, new SyncLaunchPads(LaunchPadUtils.getVisibleLaunchPads(LaunchPadLauncher.LAUNCH_PADS, p)));
        }
        return true;

    }

}
