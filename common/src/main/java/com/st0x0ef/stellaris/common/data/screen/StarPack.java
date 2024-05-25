package com.st0x0ef.stellaris.common.data.screen;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import com.st0x0ef.stellaris.client.screens.record.StarRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class StarPack extends SimpleJsonResourceReloadListener {

    public static final Map<String, StarRecord> STAR = new HashMap<>();

    public StarPack(Gson gson) {
        super(gson, "renderer/planet_screen/star");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        STAR.clear();
        object.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "stars");
            StarRecord star;

            star = StarRecord.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

            STAR.put(star.name(), star);

            int orbitColor = getColorFromName(star.orbitColor());

            CelestialBody screenStar;
                screenStar = new CelestialBody(
                        star.texture(),
                        star.name(),
                        (int) star.x(),
                        (int) star.y(),
                        (float) star.width(),
                        (float) star.height(),
                        orbitColor,
                        null
                );

            PlanetSelectionScreen.STARS.add(screenStar);
            Stellaris.LOG.info("Added a star to PlanetSelectionScreen : " + star.name());
        });
    }

    private int getColorFromName(String colorName) {
        switch (colorName) {
            case "Yellow":
                return 0xFFFF00;
            case "Red":
                return 0xFF0000;
            case "Blue":
                return 0x0000FF;
            default:
                return 0xFFFFFF;
        }
    }
}
