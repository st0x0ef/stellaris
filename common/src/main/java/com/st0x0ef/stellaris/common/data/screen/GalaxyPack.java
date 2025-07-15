package com.st0x0ef.stellaris.common.data.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.events.custom.PlanetSelectionClientEvents;
import com.st0x0ef.stellaris.client.screens.GalaxyScreen;
import com.st0x0ef.stellaris.client.screens.info.GalaxyInfo;
import com.st0x0ef.stellaris.client.screens.record.GalaxyRecord;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class GalaxyPack extends SimpleJsonResourceReloadListener {

    public static final Map<String, GalaxyRecord> GALAXY = new HashMap<>();

    public GalaxyPack() {
        super(Stellaris.GSON, "renderer/planet_screen/galaxy");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {


        object.forEach((key, value) -> {

            JsonObject json = GsonHelper.convertToJsonObject(value, "galaxy");

            GalaxyRecord galaxy = GalaxyRecord.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

            GALAXY.putIfAbsent(galaxy.name(), galaxy);

            GalaxyInfo screenGalaxy = new GalaxyInfo(
                    galaxy.texture(),
                    galaxy.name(),
                    galaxy.translatable(),
                    galaxy.id(),
                    galaxy.centerStar()
            );

            if (!GalaxyScreen.GALAXY.contains(screenGalaxy)) {
                GalaxyScreen.GALAXY.add(screenGalaxy);
            }
            Stellaris.LOG.info("Added galaxy '{}' to GalaxyScreen: {}", galaxy.name(), galaxy.id());

        });

        Stellaris.LOG.info("Finished loading {} galaxies.", GalaxyScreen.GALAXY.size());

        PlanetSelectionClientEvents.POST_GALAXY_PACK_REGISTRY.invoker().galaxyRegistered(GalaxyScreen.GALAXY);
    }
}
