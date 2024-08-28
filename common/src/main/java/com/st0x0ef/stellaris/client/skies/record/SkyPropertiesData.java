package com.st0x0ef.stellaris.client.skies.record;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.skies.PlanetSky;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
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

    public static final Map<ResourceKey<Level>, PlanetSky> SKY_PROPERTIES = new HashMap<>();

    public SkyPropertiesData() {
        super(Stellaris.GSON, "renderer/sky");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        if (Platform.getEnvironment() == Env.CLIENT) {
            object.forEach((key, value) -> {
                JsonObject json = GsonHelper.convertToJsonObject(value, "sky_renderer");
                SkyProperties skyProperties = SkyProperties.CODEC.parse(JsonOps.INSTANCE, json).get().orThrow();
                PlanetSky planetSky = new PlanetSky(skyProperties);

                SKY_PROPERTIES.putIfAbsent(skyProperties.id(), planetSky);
            });
        }
    }
}
