package com.st0x0ef.stellaris.common.data.planets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record Planet (
        String system,
        String translatable,
        String name,
        ResourceLocation dimension,
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
            ResourceLocation.CODEC.fieldOf("level").forGetter(Planet::dimension),
            Codec.BOOL.fieldOf("oxygen").forGetter(Planet::oxygen),
            Codec.FLOAT.fieldOf("temperature").forGetter(Planet::temperature),
            Codec.INT.fieldOf("distanceFromEarth").forGetter(Planet::distanceFromEarth), // in megameters
            Codec.FLOAT.fieldOf("gravity").forGetter(Planet::gravity),
            PlanetTextures.CODEC.fieldOf("textures").forGetter(Planet::textures)
    ).apply(instance, Planet::new));

    public static RegistryFriendlyByteBuf toBuffer(List<Planet> planets, final RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(planets.size());

        planets.forEach(((planet) -> {
            buffer.writeUtf(planet.system);
            buffer.writeUtf(planet.translatable);
            buffer.writeUtf(planet.name);
            buffer.writeResourceLocation(planet.dimension);
            buffer.writeBoolean(planet.oxygen);
            buffer.writeFloat(planet.temperature);
            buffer.writeInt(planet.distanceFromEarth);
            buffer.writeFloat(planet.gravity);
            planet.textures.toNetwork(buffer);
        }));

        return buffer;

    }
    public static List<Planet> readFromBuffer(RegistryFriendlyByteBuf buffer) {
        List<Planet> planets = new ArrayList<>();

        for (int i = 0; i < buffer.readInt(); i++) {
            planets.add(new Planet(
                    buffer.readUtf(),
                    buffer.readUtf(),
                    buffer.readUtf(),
                    buffer.readResourceLocation(),
                    buffer.readBoolean(),
                    buffer.readFloat(),
                    buffer.readInt(),
                    buffer.readFloat(),
                    PlanetTextures.fromNetwork(buffer)));
        }

        return planets;
    }


    public Component getTranslation() {
        return Component.translatable(this.translatable);
    }
}
