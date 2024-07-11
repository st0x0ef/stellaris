package com.st0x0ef.stellaris.client.skys.record;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.record.MoonRecord;
import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.platform.ClientUtilsPlatform;
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

    public static final Map<ResourceKey<Level>, SkyRenderer> SKY_PROPERTIES = new HashMap<>();

    public SkyPropertiesData() {
        super(Stellaris.GSON, "renderer/sky");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {

        object.forEach((key, value) -> {
//            Stellaris.LOG.info("Processing key: {}", key);
//            try {
//                JsonObject json = GsonHelper.convertToJsonObject(value, "sky_properties");
//                Stellaris.LOG.info("Loaded JSON: {}", json);
//                DataResult<SkyProperties> result = SkyProperties.CODEC.parse(JsonOps.INSTANCE, json);
//                result.resultOrPartial(error -> Stellaris.LOG.error("Failed to parse SkyProperties: " + error)).ifPresent(skyProperties -> {
//                    Stellaris.LOG.info("Adding SkyProperty: {}", skyProperties.id());
//                    SkyRenderer skyRenderer = new SkyRenderer(skyProperties);
//                    SKY_PROPERTIES.put(skyProperties.id(), skyRenderer);
//                    Stellaris.LOG.info("Loaded SkyProperty: {}", skyProperties.id());
//                });
//            } catch (Exception e) {
//                Stellaris.LOG.error("Error processing key: {}", key, e);
//            }

            JsonObject json = GsonHelper.convertToJsonObject(value, "sky_renderer");
            SkyProperties skyProperties = SkyProperties.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            SkyRenderer skyRenderer = new SkyRenderer(skyProperties);

            SKY_PROPERTIES.put(skyProperties.id(), skyRenderer);

        });
        ClientUtilsPlatform.registerPlanetsSkies(SKY_PROPERTIES);
        Stellaris.LOG.info("Finished loading sky properties. Total properties: {}", SKY_PROPERTIES.size());
    }

    public static SkyRenderer getSkyRenderersById(ResourceKey<Level> id) {
        SkyRenderer skyRenderer = SKY_PROPERTIES.get(id);
        if (skyRenderer == null) {
            Stellaris.LOG.warn("SkyProperty not found for ID: {}", id);
        }
        return skyRenderer;
    }
}
