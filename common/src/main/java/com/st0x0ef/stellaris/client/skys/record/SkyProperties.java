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
        Float cloudHeight,
        Boolean fog,
        CustomVanillaObject customVanillaObject,
        List<Weather> weather,
        Star stars,
        String skyType,
        List<SkyObject> skyObjects
) {
    public static final Codec<SkyProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("id").forGetter(SkyProperties::id),
            Codec.BOOL.fieldOf("cloud").forGetter(SkyProperties::cloud),
            Codec.FLOAT.fieldOf("cloud_height").forGetter(SkyProperties::cloudHeight),
            Codec.BOOL.fieldOf("fog").forGetter(SkyProperties::fog),
            CustomVanillaObject.CODEC.fieldOf("custom_vanilla_objects").forGetter(SkyProperties::customVanillaObject),
            Weather.CODEC.listOf().fieldOf("weather").forGetter(SkyProperties::weather),
            Star.CODEC.fieldOf("stars").forGetter(SkyProperties::stars),
            Codec.STRING.fieldOf("sky_type").forGetter(SkyProperties::skyType),
            SkyObject.CODEC.listOf().fieldOf("sky_objects").forGetter(SkyProperties::skyObjects)
    ).apply(instance, SkyProperties::new));
}