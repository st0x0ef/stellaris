package com.st0x0ef.stellaris.common.data.screen;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.info.MoonInfo;
import com.st0x0ef.stellaris.client.screens.record.MoonRecord;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class MoonPack extends SimpleJsonResourceReloadListener {

    public static final Map<String, MoonRecord> MOON = new HashMap<>();

    public MoonPack(Gson gson) {
        super(gson, "renderer/planet_screen/moon");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        MOON.clear();
        object.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "moons");
            MoonRecord moon;

            moon = MoonRecord.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

            MOON.put(moon.name(), moon);

            MoonInfo screenMoon = new MoonInfo(
                moon.texture(),
                moon.name(),
                (int) moon.distance(),
                moon.period(),
                (int) moon.width(),
                (int) moon.height(),
                PlanetSelectionScreen.findByNamePlanet(moon.parent()),
                moon.dimensionId(),
                Component.translatable(moon.translatable()),
                moon.id()
            );

            PlanetSelectionScreen.MOONS.add(screenMoon);
            Stellaris.LOG.info("Added a moon to PlanetSelectionScreen : " + moon.name());
        });
    }
}
