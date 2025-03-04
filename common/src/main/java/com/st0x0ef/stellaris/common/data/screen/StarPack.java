package com.st0x0ef.stellaris.common.data.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.event.custom.PlanetSelectionClientEvents;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import com.st0x0ef.stellaris.client.screens.record.StarRecord;
import com.st0x0ef.stellaris.common.utils.Utils;
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
public class StarPack extends SimpleJsonResourceReloadListener {

    public static final Map<String, StarRecord> STAR = new HashMap<>();
    public static int count = 0;

    public StarPack() {
        super(Stellaris.GSON, "renderer/planet_screen/star");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        if (count > 0) return;
        STAR.clear();
        object.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "stars");
            StarRecord star;

            star = StarRecord.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

            STAR.put(star.name(), star);

            int orbitColor = Utils.getColorHexCode(star.orbitColor());

            CelestialBody screenStar;
            screenStar = new CelestialBody(
                    star.texture(),
                    star.name(),
                    star.x(),
                    star.y(),
                    star.width(),
                    star.height(),
                    orbitColor,
                    null,
                    star.translatable(),
                    star.id()
            );

            for (int i = 0; i < PlanetSelectionScreen.STARS.size(); i++) {
                if (PlanetSelectionScreen.STARS.get(i).getId().equals(star.id())) {
                    PlanetSelectionScreen.STARS.set(i, screenStar);
                    Stellaris.LOG.info("Replaced existing star in PlanetSelectionScreen : {}", star.id());
                    return;
                }
            }
            PlanetSelectionScreen.STARS.add(screenStar);
            Stellaris.LOG.info("Added a new star to PlanetSelectionScreen : {}", star.id());
        });

        count++;
        PlanetSelectionClientEvents.POST_STAR_PACK_REGISTRY.invoker().starsRegistered(PlanetSelectionScreen.STARS);

    }

}