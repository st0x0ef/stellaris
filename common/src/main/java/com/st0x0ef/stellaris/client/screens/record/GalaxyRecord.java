package com.st0x0ef.stellaris.client.screens.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record GalaxyRecord(
        ResourceLocation texture, String name, String translatable, String id, String centerStar) {


    public static final Codec<GalaxyRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(GalaxyRecord::texture),
            Codec.STRING.fieldOf("name").forGetter(GalaxyRecord::name),
            Codec.STRING.fieldOf("translatable").forGetter(GalaxyRecord::translatable),
            Codec.STRING.fieldOf("id").forGetter(GalaxyRecord::id),
            Codec.STRING.fieldOf("centerStar").forGetter(GalaxyRecord::centerStar)
    ).apply(instance, GalaxyRecord::new));
}