package com.st0x0ef.stellaris.common.data.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.events.custom.PlanetSelectionClientEvents;
import com.st0x0ef.stellaris.client.screens.PlanetSelectionScreen;
import com.st0x0ef.stellaris.client.screens.info.PSystemInfo;
import com.st0x0ef.stellaris.client.screens.record.PSystemRecord;
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
public class PSystemPack extends SimpleJsonResourceReloadListener {

    public static final Map<String, PSystemRecord> PSYSTEM = new HashMap<>();

    public PSystemPack() {
        super(Stellaris.GSON, "renderer/planet_screen/planetary_system");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        PSYSTEM.clear();
        PlanetSelectionScreen.PSYSTEMS.clear();

        object.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "planetary_systems");

            PSystemRecord.CODEC.parse(JsonOps.INSTANCE, json).result().ifPresentOrElse(record -> {
                PSYSTEM.put(record.name(), record);

                PSystemInfo screenPSYSTEM = new PSystemInfo(
                        record.name(),
                        record.translatable(),
                        record.parent(),
                        record.id(),
                        record.stars(),
                        record.centerX(),
                        record.centerY()
                );

                PlanetSelectionScreen.PSYSTEMS.add(screenPSYSTEM);
                Stellaris.LOG.info("Added a planetary system '{}' to PlanetScreen: {}", record.name(), record.id());

            }, () -> {
                Stellaris.LOG.error("Failed to parse PSystemRecord JSON: {}", key.toString());
            });
        });

        Stellaris.LOG.info("Finished loading {} Planetary System.", PlanetSelectionScreen.PSYSTEMS.size());

        PlanetSelectionClientEvents.POST_P_SYSTEM_PACK_REGISTRY_EVENT_EVENT
                .invoker()
                .PSystemRegistered(PlanetSelectionScreen.PSYSTEMS);
    }
}
