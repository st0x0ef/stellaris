package com.st0x0ef.stellaris.client.skys.record;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
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

public class SkyPropertiesData extends SimpleJsonResourceReloadListener {

    public static final Map<ResourceKey<Level>, SkyProperties> SKY_PROPERTIES = new HashMap<>();
    private static int reloadCount = 0;

    public SkyPropertiesData() {
        super(Stellaris.GSON, "renderer/sky");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        reloadCount++;
        Stellaris.LOG.info("Starting to load sky properties - reload count: {}", reloadCount);

        if (reloadCount == 1) {
            SKY_PROPERTIES.clear();
            Stellaris.LOG.info("Cleared SKY_PROPERTIES map. Current size: {}", SKY_PROPERTIES.size());
        }

        object.forEach((key, value) -> {
            Stellaris.LOG.info("Processing key: {}", key);
            try {
                JsonObject json = GsonHelper.convertToJsonObject(value, "sky_properties");
                Stellaris.LOG.info("Loaded JSON: {}", json);
                DataResult<SkyProperties> result = SkyProperties.CODEC.parse(JsonOps.INSTANCE, json);
                result.resultOrPartial(error -> Stellaris.LOG.error("Failed to parse SkyProperties: " + error)).ifPresent(skyProperties -> {
                    Stellaris.LOG.info("Adding SkyProperty: {}", skyProperties.id());
                    SKY_PROPERTIES.put(skyProperties.id(), skyProperties);
                    Stellaris.LOG.info("Loaded SkyProperty: {}", skyProperties.id());
                });
            } catch (Exception e) {
                Stellaris.LOG.error("Error processing key: {}", key, e);
            }
        });

        Stellaris.LOG.info("Finished loading sky properties. Total properties: {}", SKY_PROPERTIES.size());
    }

    public static SkyProperties getSkyPropertiesById(ResourceKey<Level> id) {
        Stellaris.LOG.info("Getting SkyProperty by ID: {}", id);
        SkyProperties properties = SKY_PROPERTIES.get(id);
        if (properties == null) {
            Stellaris.LOG.warn("SkyProperty not found for ID: {}", id);
        }
        return properties;
    }
}
