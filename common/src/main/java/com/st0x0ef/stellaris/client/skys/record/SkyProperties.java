package com.st0x0ef.stellaris.client.skys.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

public record SkyProperties(
        ResourceKey<Level> id,
        Boolean cloud,
        Boolean fog,
        CustomVanillaObject customVanillaObject,
        List<Weather> weather,
        int sunriseColor,
        Star stars,
        List<SkyObject> skyObjects
) {
    public static final Codec<SkyProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("id").forGetter(SkyProperties::id),
            Codec.BOOL.fieldOf("cloud").forGetter(SkyProperties::cloud),
            Codec.BOOL.fieldOf("fog").forGetter(SkyProperties::fog),
            CustomVanillaObject.CODEC.fieldOf("custom_vanilla_objects").forGetter(SkyProperties::customVanillaObject),

            Weather.CODEC.listOf().fieldOf("weather").forGetter(SkyProperties::weather),
            Codec.INT.fieldOf("sunrise_color").forGetter(SkyProperties::sunriseColor),
            Star.CODEC.fieldOf("stars").forGetter(SkyProperties::stars),
            SkyObject.CODEC.listOf().fieldOf("sky_objects").forGetter(SkyProperties::skyObjects)
    ).apply(instance, SkyProperties::new));
}
