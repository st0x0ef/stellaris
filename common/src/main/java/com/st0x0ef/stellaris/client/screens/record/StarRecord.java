package com.st0x0ef.stellaris.client.screens.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record StarRecord(
        ResourceLocation texture, String name,
        float x, float y, float width,
        float height, String orbitColor) {


    public static final Codec<StarRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(StarRecord::texture),
            Codec.STRING.fieldOf("name").forGetter(StarRecord::name),
            Codec.FLOAT.fieldOf("x").forGetter(StarRecord::x),
            Codec.FLOAT.fieldOf("y").forGetter(StarRecord::y),
            Codec.FLOAT.fieldOf("width").forGetter(StarRecord::width),
            Codec.FLOAT.fieldOf("height").forGetter(StarRecord::height),
            Codec.STRING.fieldOf("orbitColor").forGetter(StarRecord::orbitColor)
    ).apply(instance, StarRecord::new));

}