package com.st0x0ef.stellaris.common.data.planets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record PlanetTextures(
        ResourceLocation planet_bar,
        ResourceLocation planet

) {
    public static final Codec<PlanetTextures> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("planet_bar").forGetter(PlanetTextures::planet_bar),
            ResourceLocation.CODEC.fieldOf("planet").forGetter(PlanetTextures::planet)
    ).apply(instance, PlanetTextures::new));

    public static PlanetTextures fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new PlanetTextures(buffer.readResourceLocation(), buffer.readResourceLocation());
    }

    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.planet_bar);
        buffer.writeResourceLocation(this.planet);
        //return buffer;
    }
}
