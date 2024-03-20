package com.st0x0ef.stellaris.common.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.platform.Platform;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomConfig {
    public static Map<String, ConfigEntry> CONFIG = new HashMap<>();

    public static void init() {
        loadConfigFile();
        addEntries();
        writeConfigFile(CONFIG, "stellaris.json");
        //config = Stellaris.GSON.fromJson(Platform.getStreamForResource("stellaris.json"), Map.class);

    }

    public static void addEntries() {

        addEntry("test", new ConfigEntry.ConfigEntryString("1", "uwu"));
        addEntry("test2", new ConfigEntry.ConfigEntryString("1", "uwu"));
        addEntry("test3", new ConfigEntry.ConfigEntryString("1", "uwu"));

    }

    public static void addEntry(String name, ConfigEntry entry) {
        Stellaris.LOG.error(name);

        if(CONFIG.get(name) == null) {

            CONFIG.put(name, entry);
        } else {

            Stellaris.LOG.error(CONFIG.get(name).getName());
            Stellaris.LOG.error(entry.getName());

            CONFIG.put(name, new ConfigEntry.ConfigEntryString(CONFIG.get(name).getName(), CONFIG.get(name).getDescription()));
        }


    }

    public static void writeConfigFile(Map<?, ?> config, String path) {
        try {
            FileWriter file = new FileWriter(Platform.getConfigFolder() + "/" + path);
            file.write(Stellaris.GSON.toJson(config));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfigFile() {

        String jsonString;
        HashMap<String, ConfigEntry> map = new HashMap<>();

        try {
            jsonString = readFileAsString(Platform.getConfigFolder() + "/stellaris.json");
            JsonObject jsonObject = Stellaris.GSON.fromJson(jsonString, JsonObject.class);

            jsonObject.getAsJsonObject().entrySet().forEach(entry -> {
                String key = entry.getKey();
                JsonObject value = entry.getValue().getAsJsonObject();
                if(jsonObject == null) {
                    return;
                }
                CONFIG.put(key, new ConfigEntry.ConfigEntryString(value.get("value").toString(), value.get("description").getAsString()));

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
