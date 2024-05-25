package com.st0x0ef.stellaris.client.screens.info;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class PlanetInfo extends CelestialBody {
    private final long orbitDuration;
    public final CelestialBody orbitCenter;
    public final double orbitRadius;
    public double currentAngle;


    public PlanetInfo(ResourceLocation texture, String name, double orbitRadius, long orbitDuration, int width, int height, CelestialBody orbitCenter, ResourceKey<Level> dimension) {
        super(texture, name, 0, 0, width, height, 0xFFFFFF, dimension);
        this.orbitRadius = orbitRadius;
        this.orbitDuration = orbitDuration;
        this.orbitCenter = orbitCenter;
        this.currentAngle = 0;
    }

    public void updateAngle(long currentTime) {
        double orbitProgress = (currentTime % orbitDuration) / (double) orbitDuration;
        this.currentAngle = orbitProgress * 2 * Math.PI;
    }

    public void updatePosition() {
        this.x = (float) (orbitCenter.x + orbitRadius * Math.cos(currentAngle));
        this.y = (float) (orbitCenter.y + orbitRadius * Math.sin(currentAngle));
    }
}