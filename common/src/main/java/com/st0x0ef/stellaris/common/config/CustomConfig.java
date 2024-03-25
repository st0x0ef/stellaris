package com.st0x0ef.stellaris.common.config;

import com.google.gson.JsonObject;
import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.platform.Platform;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CustomConfig {

    public static Map<String, ConfigEntry<?>> CONFIG = new HashMap<>();

    public static void init() {
        loadConfigFile();
        addEntries();
        writeConfigFile("stellaris.json");

    }


    public static void addEntries() {

        addEntry("test", new ConfigEntry<Boolean>(true, "This is a test entry"));
        addEntry("test2", new ConfigEntry<>(true, "This is a test entry"));
        addEntry("test3", new ConfigEntry<>("true", "Coool"));
    }

    public static void addEntry(String name, ConfigEntry<?> entry) {

        if(CONFIG.get(name) == null) {
            CONFIG.put(name, entry);
        } else {

            CONFIG.put(name, new ConfigEntry(CONFIG.get(name).getValue(), entry.getDescription()));
        }

    }

    public static void writeConfigFile(String path) {
        try {
            FileWriter file = new FileWriter(Platform.getConfigFolder() + "/" + path);
            file.write(Stellaris.GSON.toJson(CONFIG));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfigFile() {

        String jsonString;

        try {
            jsonString = readFileAsString(Platform.getConfigFolder() + "/stellaris.json");
            JsonObject jsonObject = Stellaris.GSON.fromJson(jsonString, JsonObject.class);

            jsonObject.getAsJsonObject().entrySet().forEach(entry -> {
                ConfigEntry<?> obj2 = Stellaris.GSON.fromJson(entry.getValue(), ConfigEntry.class);
                CONFIG.put(entry.getKey(), obj2);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String readFileAsString(String file) throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

}