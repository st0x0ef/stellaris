package com.st0x0ef.stellaris.common.data.planets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record Planet (
        String system,
        String translatable,
        String name,
        ResourceKey<Level> dimension,
        ResourceKey<Level> orbit,
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
            ResourceKey.codec(Registries.DIMENSION).fieldOf("level").forGetter(Planet::dimension),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("orbit").forGetter(Planet::orbit),
            Codec.BOOL.fieldOf("oxygen").forGetter(Planet::oxygen),
            Codec.FLOAT.fieldOf("temperature").forGetter(Planet::temperature),
            Codec.INT.fieldOf("distanceFromEarth").forGetter(Planet::distanceFromEarth), // in megameters
            Codec.FLOAT.fieldOf("gravity").forGetter(Planet::gravity),
            PlanetTextures.CODEC.fieldOf("textures").forGetter(Planet::textures)
    ).apply(instance, Planet::new));

    public static Planet fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new Planet(buffer.readUtf(), buffer.readUtf(), buffer.readUtf(), buffer.readResourceKey(Registries.DIMENSION), buffer.readResourceKey(Registries.DIMENSION), buffer.readBoolean(), buffer.readFloat(), buffer.readInt(), buffer.readFloat(), PlanetTextures.fromNetwork(buffer));
    }

    public RegistryFriendlyByteBuf toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeUtf(this.system);
        buffer.writeUtf(this.translatable);
        buffer.writeUtf(this.name);
        buffer.writeResourceKey(this.dimension);
        buffer.writeResourceKey(this.orbit);
        buffer.writeBoolean(this.oxygen);
        buffer.writeFloat(this.temperature);
        buffer.writeInt(this.distanceFromEarth);
        buffer.writeFloat(this.gravity);
        this.textures.toNetwork(buffer);
        return buffer;
    }

    public Component getTranslation() {
        return Component.translatable(this.translatable);
    }
}
