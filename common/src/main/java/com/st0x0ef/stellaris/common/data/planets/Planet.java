package com.st0x0ef.stellaris.common.data.planets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record Planet(
        String system,
        ResourceKey<Level> dimension,
        ResourceKey<Level> orbit,
        boolean oxygen, int temperature, int distanceFromEarth, float gravity
) {
    public static final Codec<Planet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("system").forGetter(Planet::system),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("level").forGetter(Planet::dimension),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("orbit").forGetter(Planet::orbit),
            Codec.BOOL.fieldOf("oxygen").forGetter(Planet::oxygen),
            Codec.INT.fieldOf("temperature").forGetter(Planet::temperature),
            Codec.INT.fieldOf("distanceFromEarth").forGetter(Planet::distanceFromEarth),
            Codec.FLOAT.fieldOf("gravity").forGetter(Planet::gravity)
    ).apply(instance, Planet::new));

    public static Planet fromNetwork(FriendlyByteBuf buffer) {
        return new Planet(buffer.readUtf(), buffer.readResourceKey(Registries.DIMENSION), buffer.readResourceKey(Registries.DIMENSION), buffer.readBoolean(), buffer.readInt(), buffer.readInt(), buffer.readFloat());
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.system);
        buffer.writeResourceKey(this.dimension);
        buffer.writeResourceKey(this.orbit);
        buffer.writeBoolean(this.oxygen);
        buffer.writeInt(this.temperature);
        buffer.writeInt(this.distanceFromEarth);
        buffer.writeFloat(this.gravity);

    }
}
