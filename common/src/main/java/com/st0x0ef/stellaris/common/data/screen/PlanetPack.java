package com.st0x0ef.stellaris.common.data.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.events.custom.PlanetSelectionClientEvents;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.info.PlanetInfo;
import com.st0x0ef.stellaris.client.screens.record.PlanetRecord;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class PlanetPack extends SimpleJsonResourceReloadListener {

    public PlanetPack() {
        super(Stellaris.GSON, "renderer/planet_screen/planet");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        object.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "planets");
            PlanetRecord planet = PlanetRecord.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

            PlanetInfo screenPlanet = new PlanetInfo(
                    planet.texture(),
                    planet.name(),
                    planet.distance(),
                    planet.period(),
                    planet.width(),
                    planet.height(),
                    PlanetSelectionScreen.findByNameStar(planet.parent()),
                    planet.dimensionId(),
                    Component.translatable(planet.translatable()),
                    planet.id(),
                    planet.spaceStation().orElse(false),
                    planet.canLaunchOn().orElse(true)
            );

            planet.spaceStation().ifPresent(screenPlanet::setSpaceStation);
            planet.canLaunchOn().ifPresent(screenPlanet::setCanLaunchOn);


            for (int i = 0; i < PlanetSelectionScreen.PLANETS.size(); i++) {
                if (PlanetSelectionScreen.PLANETS.get(i).getId().equals(planet.id())) {
                    PlanetSelectionScreen.PLANETS.set(i, screenPlanet);
                    Stellaris.LOG.info("Replaced existing planet in PlanetSelectionScreen : {}", planet.name());
                    return;
                }
            }
            PlanetSelectionScreen.PLANETS.add(screenPlanet);
            Stellaris.LOG.info("Added a new planet to PlanetSelectionScreen : {}", planet.name());
        });
        PlanetSelectionClientEvents.POST_PLANET_PACK_REGISTRY.invoker().planetRegistered(PlanetSelectionScreen.PLANETS);

    }
}