package com.st0x0ef.stellaris.client.screens.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record PlanetRecord(
        ResourceLocation texture, String name,
        float distance, long period, float width,
        float height, String parent) {


    public static final Codec<PlanetRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(PlanetRecord::texture),
            Codec.STRING.fieldOf("name").forGetter(PlanetRecord::name),
            Codec.FLOAT.fieldOf("distance").forGetter(PlanetRecord::distance),
            Codec.LONG.fieldOf("period").forGetter(PlanetRecord::period),
            Codec.FLOAT.fieldOf("width").forGetter(PlanetRecord::width),
            Codec.FLOAT.fieldOf("height").forGetter(PlanetRecord::height),
            Codec.STRING.fieldOf("parent").forGetter(PlanetRecord::parent)
    ).apply(instance, PlanetRecord::new));
}