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

    public static final Map<ResourceLocation, Planet> PLANETS = new HashMap<>();
    public static final Map<String, ResourceLocation> SYSTEMS = new HashMap<>();

    public StellarisData() {
        super(Stellaris.GSON, "planets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        PLANETS.clear();
        SYSTEMS.clear();
        resourceLocationJsonElementMap.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "planets");
            Planet planet = Planet.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            PLANETS.put(planet.dimension(), planet);
            SYSTEMS.put(planet.system(), planet.dimension());
        });
    }

    public static void addPlanet(ResourceLocation location, Planet planet) {
        PLANETS.put(location, planet);
    }

    public static Planet getPlanet(ResourceLocation location) {
        return PLANETS.get(location);
    }
}
