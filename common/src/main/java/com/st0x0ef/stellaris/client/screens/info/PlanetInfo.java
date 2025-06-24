package com.st0x0ef.stellaris.client.screens.info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class PlanetInfo extends CelestialBody {
    private final long orbitDuration;
    public final CelestialBody orbitCenter;
    public final double orbitRadius;
    public double currentAngle;
    public boolean spaceStation = false;
    public boolean canLaunchOn = true;

    public PlanetInfo(ResourceLocation texture, String name, double orbitRadius, long orbitDuration, float width, float height, CelestialBody orbitCenter, ResourceKey<Level> dimension, Component translatable, String id, boolean spaceStation, boolean canLaunchOn) {
        this(texture, name, orbitRadius, orbitDuration, width, height, orbitCenter, dimension.location(), translatable.getString(), id, spaceStation, canLaunchOn);
    }

    public PlanetInfo(ResourceLocation texture, String name, double orbitRadius, long orbitDuration, float width, float height, CelestialBody orbitCenter, ResourceLocation dimension, String translatable, String id, boolean spaceStation, boolean canLaunchOn) {
        super(texture, name, 0, 0, width, height, 0xFFFFFF, dimension, translatable, id);
        this.orbitRadius = orbitRadius;
        this.orbitDuration = orbitDuration;
        this.orbitCenter = orbitCenter;
        this.currentAngle = 0;
        this.translatable = translatable;
        this.id = id;
    }

    public double updateAngle(long currentTime) {
        double orbitProgress = (currentTime % orbitDuration) / (double) orbitDuration;
        this.currentAngle = orbitProgress * 2 * Math.PI;

        return this.currentAngle;
    }

    public void updatePosition() {
        this.x = (float) (orbitCenter.x + orbitRadius * Math.cos(currentAngle));
        this.y = (float) (orbitCenter.y + orbitRadius * Math.sin(currentAngle));
    }

    public long getOrbitDuration() {
        return this.orbitDuration;
    }

    public static final Codec<PlanetInfo> CODEC = RecordCodecBuilder.create(
            instance ->
                    instance
                            .group(
                                    ResourceLocation.CODEC.fieldOf("texture").forGetter(b -> b.texture),
                                    Codec.STRING.fieldOf("name").forGetter(b -> b.name),
                                    Codec.DOUBLE.fieldOf("orbitRadius").forGetter(b -> b.orbitRadius),
                                    Codec.LONG.fieldOf("orbitDuration").forGetter(b -> b.orbitDuration),
                                    Codec.FLOAT.fieldOf("width").forGetter(b -> b.width),
                                    Codec.FLOAT.fieldOf("height").forGetter(b -> b.height),
                                    CelestialBody.CODEC.fieldOf("orbitCenter").forGetter(b -> b.orbitCenter),
                                    ResourceLocation.CODEC.fieldOf("dimension").forGetter(b -> b.dimension),
                                    Codec.STRING.fieldOf("translatable").forGetter(b -> b.translatable),
                                    Codec.STRING.fieldOf("id").forGetter(b -> b.id),
                                    Codec.BOOL.optionalFieldOf("space_station", false).forGetter(b -> b.spaceStation),
                                    Codec.BOOL.optionalFieldOf("can_launch_on", true).forGetter(b -> b.canLaunchOn)
                            ).apply(instance, PlanetInfo::new)
    );
}