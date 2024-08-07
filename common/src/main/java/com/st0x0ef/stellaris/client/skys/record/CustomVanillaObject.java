package com.st0x0ef.stellaris.client.skys.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record CustomVanillaObject(
        boolean sun, ResourceLocation sunTexture,
        boolean moon,  ResourceLocation moonTexture
) {


    public static final Codec<CustomVanillaObject> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("sun").forGetter(CustomVanillaObject::sun),
            ResourceLocation.CODEC.fieldOf("sun_texture").forGetter(CustomVanillaObject::sunTexture),

            Codec.BOOL.fieldOf("moon").forGetter(CustomVanillaObject::moon),
            ResourceLocation.CODEC.fieldOf("moon_texture").forGetter(CustomVanillaObject::moonTexture)
    ).apply(instance, CustomVanillaObject::new));
}
