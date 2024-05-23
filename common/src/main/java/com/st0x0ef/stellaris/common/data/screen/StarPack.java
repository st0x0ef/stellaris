package com.st0x0ef.stellaris.common.data.screen;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.record.StarRecord;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class StarPack extends SimpleJsonResourceReloadListener {

    public static final Map<String, StarRecord> STAR = new HashMap<>();

    public StarPack(Gson gson) {
        super(gson, "renderer/planet_screen/star");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        STAR.clear();
        object.forEach((key, value) -> {
            JsonObject json = GsonHelper.convertToJsonObject(value, "planets");
            StarRecord star = StarRecord.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
            STAR.put(star.name(), star);
            Stellaris.LOG.error("Adding" + star.name());
        });
    }
}