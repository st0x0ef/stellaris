package com.st0x0ef.stellaris.common.data.planets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class StellarisData extends SimpleJsonResourceReloadListener {

    public static final Map<ResourceKey<Level>, Planet> PLANETS = new HashMap<>();

    public StellarisData() {
        super(Stellaris.GSON, "planets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        PLANETS.clear();
        resourceLocationJsonElementMap.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "planets");
            Planet planet = Planet.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, Stellaris.LOG::error);
            PLANETS.put(planet.dimension(), planet);

        });
    }

    public static Planet getPlanet(ResourceKey<Level> level) {
        return PLANETS.get(level);
    }

    public static boolean isPlanet(ResourceKey<Level> level) {
        return PLANETS.containsKey(level);
    }
}
