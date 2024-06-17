package com.st0x0ef.stellaris.common.data.renderer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.skys.record.Renderable;
import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import com.st0x0ef.stellaris.client.skys.type.RenderableType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class SkyPack extends SimpleJsonResourceReloadListener {

    public static final Map<String, Renderable> RENDERABLE_MAP = new HashMap<>();

    public SkyPack() {
        super(Stellaris.GSON, "renderer/sky");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        RENDERABLE_MAP.clear();

        Stellaris.LOG.info("Starting JSON parsing for sky renderers");

        object.forEach((key, value) -> {
            Stellaris.LOG.info("Processing key: {}", key);
            JsonObject json = GsonHelper.convertToJsonObject(value, "renderable");
            Renderable renderable;

            try {
                renderable = Renderable.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(error -> {
                    Stellaris.LOG.error("Failed to parse renderable for {}: {}", key, error);
                }).orElse(null);

                if (renderable != null) {
                    Stellaris.LOG.info("Parsed renderable: {}", renderable);
                    RENDERABLE_MAP.put(renderable.dimension().toString(), renderable);

                    RenderableType renderableType = new RenderableType(
                            renderable.dimension(),
                            renderable.cloudType(),
                            renderable.weather(),
                            renderable.sunriseColor(),
                            renderable.star().getFirst(),
                            renderable.skyObject()
                    );

                    SkyRenderer.renderableList.add(renderableType);
                } else {
                    Stellaris.LOG.warn("Parsed renderable is null for key: {}", key);
                }
            } catch (Exception e) {
                Stellaris.LOG.error("Exception occurred while parsing renderable for {}: {}", key, e.getMessage(), e);
            }
        });

        Stellaris.LOG.info("Completed JSON parsing for sky renderers");
    }
}
