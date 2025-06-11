package com.st0x0ef.stellaris.common.config;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.platform.Platform;

import java.io.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class ConfigManager {

    public static CommonConfig loadOrGenerateDefaults() {
        Path systemsFile =  Platform.getConfigFolder().resolve("stellaris-config.json");

        try {
            BufferedReader reader = Files.newBufferedReader(systemsFile);
            CommonConfig config = Stellaris.GSON.fromJson(reader, CommonConfig.class);

            Writer writer = new FileWriter(systemsFile.toFile());
            Stellaris.GSON.toJson(config, writer);
            writer.close();

            return config;

        } catch (Exception e) {

            if (!(e instanceof NoSuchFileException))
                e.printStackTrace();

            try {
                File folder = systemsFile.toFile().getParentFile();
                if (!folder.exists())
                    folder.mkdirs();

                Writer writer = new FileWriter(systemsFile.toFile());
                Stellaris.GSON.toJson(new CommonConfig(), writer);
                writer.close();

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

        return new CommonConfig();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface InnerConfig {
    }
}
