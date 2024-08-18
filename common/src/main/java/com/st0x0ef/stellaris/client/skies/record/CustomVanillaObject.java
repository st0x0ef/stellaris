package com.st0x0ef.stellaris.client.skies.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record CustomVanillaObject(
        boolean sun, ResourceLocation sunTexture, float sunSize,
        boolean moon, boolean moonPhase,  ResourceLocation moonTexture, float moonSize
) {


    public static final Codec<CustomVanillaObject> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("sun").forGetter(CustomVanillaObject::sun),
            ResourceLocation.CODEC.fieldOf("sun_texture").forGetter(CustomVanillaObject::sunTexture),
            Codec.FLOAT.fieldOf("sun_height").forGetter(CustomVanillaObject::sunSize),
            Codec.BOOL.fieldOf("moon").forGetter(CustomVanillaObject::moon),
            Codec.BOOL.fieldOf("moon_phase").forGetter(CustomVanillaObject::moonPhase),
            ResourceLocation.CODEC.fieldOf("moon_texture").forGetter(CustomVanillaObject::moonTexture),
            Codec.FLOAT.fieldOf("moon_height").forGetter(CustomVanillaObject::moonSize)
            ).apply(instance, CustomVanillaObject::new));
}
