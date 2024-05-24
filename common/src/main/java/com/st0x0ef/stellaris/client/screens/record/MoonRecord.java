package com.st0x0ef.stellaris.client.screens.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record MoonRecord(
        ResourceLocation texture, String name,
        float distance, long period, float width,
        float height, String parent, String dimensionId) {


    public static final Codec<MoonRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(MoonRecord::texture),
            Codec.STRING.fieldOf("name").forGetter(MoonRecord::name),
            Codec.FLOAT.fieldOf("distance").forGetter(MoonRecord::distance),
            Codec.LONG.fieldOf("period").forGetter(MoonRecord::period),
            Codec.FLOAT.fieldOf("width").forGetter(MoonRecord::width),
            Codec.FLOAT.fieldOf("height").forGetter(MoonRecord::height),
            Codec.STRING.fieldOf("parent").forGetter(MoonRecord::parent),
            Codec.STRING.fieldOf("dimensionId").forGetter(MoonRecord::dimensionId)
    ).apply(instance, MoonRecord::new));
}