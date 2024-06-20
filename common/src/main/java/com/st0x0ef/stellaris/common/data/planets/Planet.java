package com.st0x0ef.stellaris.common.data.planets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record Planet (
        String system,
        String translatable,
        String name,
        ResourceKey<Level> dimension,
        boolean oxygen,
        float temperature,
        int distanceFromEarth,
        float gravity,
        PlanetTextures textures

) {
    public static final Codec<Planet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("system").forGetter(Planet::system),
            Codec.STRING.fieldOf("translatable").forGetter(Planet::translatable),
            Codec.STRING.fieldOf("name").forGetter(Planet::name),
            Level.RESOURCE_KEY_CODEC.fieldOf("level").forGetter(Planet::dimension),
            Codec.BOOL.fieldOf("oxygen").forGetter(Planet::oxygen),
            Codec.FLOAT.fieldOf("temperature").forGetter(Planet::temperature),
            Codec.INT.fieldOf("distanceFromEarth").forGetter(Planet::distanceFromEarth), // in megameters
            Codec.FLOAT.fieldOf("gravity").forGetter(Planet::gravity),
            PlanetTextures.CODEC.fieldOf("textures").forGetter(Planet::textures)
    ).apply(instance, Planet::new));

    public Component getTranslation() {
        return Component.translatable(this.translatable);
    }
}
