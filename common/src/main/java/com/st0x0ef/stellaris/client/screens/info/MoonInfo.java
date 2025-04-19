package com.st0x0ef.stellaris.client.screens.info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public class MoonInfo extends CelestialBody {

    public final PlanetInfo orbitCenter;
    public final long orbitalPeriod;
    public final double orbitRadius;
    public double currentAngle;

    public MoonInfo(ResourceLocation texture, String name, double orbitRadius, long orbitalPeriod, int width, int height, PlanetInfo orbitCenter, ResourceLocation dimension, String translatable, String id) {
        super(texture, name, 0, 0, width, height, 0xFFFFFF, dimension, translatable, id);
        this.orbitRadius = orbitRadius;
        this.orbitalPeriod = orbitalPeriod;
        this.orbitCenter = orbitCenter;
        this.currentAngle = 0;
        this.translatable = translatable;
        this.id = id;
    }

    public MoonInfo setClickable(boolean clickable) {
        this.clickable = clickable;
        return this;
    }

    public double updateAngle(long currentTime) {
        this.currentAngle = (currentTime % orbitalPeriod) * (2 * Math.PI / orbitalPeriod);

        return this.currentAngle;
    }

    public void updatePosition() {
        this.x = (float) (orbitCenter.x + orbitRadius * Math.cos(currentAngle));
        this.y = (float) (orbitCenter.y + orbitRadius * Math.sin(currentAngle));
    }

    public static final Codec<MoonInfo> CODEC = RecordCodecBuilder.create(
            instance ->
                    instance
                            .group(
                                    ResourceLocation.CODEC.fieldOf("texture").forGetter(b -> b.texture),
                                    Codec.STRING.fieldOf("name").forGetter(b -> b.name),
                                    Codec.DOUBLE.fieldOf("orbitRadius").forGetter(b -> b.orbitRadius),
                                    Codec.LONG.fieldOf("orbitalPeriod").forGetter(b -> (long) b.y),
                                    Codec.INT.fieldOf("width").forGetter(b -> (int) b.width),
                                    Codec.INT.fieldOf("height").forGetter(b -> (int) b.height),
                                    PlanetInfo.CODEC.fieldOf("orbitCenter").forGetter(b -> b.orbitCenter),
                                    ResourceLocation.CODEC.fieldOf("dimension").forGetter(b -> b.dimension),
                                    Codec.STRING.fieldOf("translatable").forGetter(b -> b.translatable),
                                    Codec.STRING.fieldOf("id").forGetter(b -> b.id)
                            )
                            .apply(instance, MoonInfo::new)
    );

}
