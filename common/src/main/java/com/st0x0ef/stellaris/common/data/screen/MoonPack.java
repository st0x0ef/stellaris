package com.st0x0ef.stellaris.common.data.screen;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.events.custom.PlanetSelectionClientEvents;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.info.MoonInfo;
import com.st0x0ef.stellaris.client.screens.record.MoonRecord;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class MoonPack extends SimpleJsonResourceReloadListener<MoonRecord> {

    public static final Map<String, MoonRecord> MOON = new HashMap<>();
    public static int count = 0;

    public MoonPack() {
        super(MoonRecord.CODEC, FileToIdConverter.json("renderer/planet_screen/moon"));
    }

    @Override
    protected void apply(Map<ResourceLocation, MoonRecord> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        if (count > 0) return;
        MOON.clear();
        object.forEach((key, moon) -> {
            MOON.put(moon.name(), moon);

            MoonInfo screenMoon = new MoonInfo(
                    moon.texture(),
                    moon.name(),
                    (int) moon.distance(),
                    moon.period(),
                    (int) moon.width(),
                    (int) moon.height(),
                    PlanetSelectionScreen.findByNamePlanet(moon.parent()),
                    moon.dimensionId().location(),
                    moon.translatable(),
                    moon.id()
            );

            moon.clickable().ifPresent(screenMoon::setClickable);

            for (int i = 0; i < PlanetSelectionScreen.MOONS.size(); i++) {
                if (PlanetSelectionScreen.MOONS.get(i).getId().equals(screenMoon.getId())) {
                    PlanetSelectionScreen.MOONS.set(i, screenMoon);
                    Stellaris.LOG.info("Replaced existing moon in PlanetSelectionScreen : {}", moon.name());
                    return;
                }
            }
            PlanetSelectionScreen.MOONS.add(screenMoon);
            Stellaris.LOG.info("Added a new moon to PlanetSelectionScreen : {}", moon.name());
        });
        PlanetSelectionClientEvents.POST_MOON_PACK_REGISTRY.invoker().moonRegistered(PlanetSelectionScreen.MOONS);

        count++;

    }
}