package com.st0x0ef.stellaris.common.launchpads;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.util.GsonHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

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

            try {
                File folder = systemsFile.toFile().getParentFile();
                if (!folder.exists())
                    folder.mkdirs();

                LaunchPad.LaunchPadContainer defaults = LaunchPad.LaunchPadContainer.DEFAULT;
                JsonElement jsonElement = LaunchPad.LaunchPadContainer.toJson(defaults);
                String launchpadsFile = Stellaris.GSON.toJson(jsonElement);

                var launchpadsWrite = Files.newBufferedWriter(systemsFile);
                launchpadsWrite.write(launchpadsFile);
                launchpadsWrite.close();

                LaunchPadLauncher.LAUNCH_PADS = defaults;

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }


    public static void addLaunchPad(LaunchPad pad, Path worldPath) throws IOException {
        Path launchpath = worldPath.resolve("launch-pads.json");


        LaunchPadLauncher.LAUNCH_PADS.launchPads().add(pad);

        JsonElement jsonElement = LaunchPad.LaunchPadContainer.toJson(LaunchPadLauncher.LAUNCH_PADS);
        String launchpadsFile = Stellaris.GSON.toJson(jsonElement);

        var launchpadsWrite = Files.newBufferedWriter(launchpath);
        launchpadsWrite.write(launchpadsFile);
        launchpadsWrite.close();

    }

}
