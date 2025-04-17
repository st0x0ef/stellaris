package com.st0x0ef.stellaris.common.data.planets;

import com.st0x0ef.stellaris.common.events.custom.PlanetEvents;
import dev.architectury.event.EventResult;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StellarisData extends SimpleJsonResourceReloadListener<Planet> {

    private static final List<Planet> PLANETS = new ArrayList<>();

    public StellarisData() {
        super(Planet.CODEC, FileToIdConverter.json("planets"));
    }

    @Override
    protected void apply(Map<ResourceLocation, Planet> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        PLANETS.clear();
        resourceLocationJsonElementMap.forEach((key, planet) -> {
            if (PlanetEvents.PLANET_REGISTERED.invoker().planetRegistered(planet) == EventResult.interruptDefault() || PlanetEvents.PLANET_REGISTERED.invoker().planetRegistered(planet) ==  EventResult.interruptFalse()) {
                return;
            }
            PLANETS.add(planet);
        });
        PlanetEvents.POST_PLANET_REGISTRY.invoker().planetRegistered(PLANETS, false);
    }

    public static List<Planet> getPlanets() {
        return PLANETS;
    }

    public static void addAllPlanets(List<Planet> planets) {
        PLANETS.clear();
        PLANETS.addAll(planets);
        PlanetEvents.POST_PLANET_REGISTRY.invoker().planetRegistered(planets, true);
    }

    public static void addPlanets(List<Planet> planets) {
        PLANETS.addAll(planets);
    }

}
