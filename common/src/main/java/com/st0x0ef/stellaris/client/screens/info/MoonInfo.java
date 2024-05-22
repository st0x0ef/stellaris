package com.st0x0ef.stellaris.client.screens.info;

import net.minecraft.resources.ResourceLocation;

public class MoonInfo extends CelestialBody {
    private final long orbitDuration;
    public final CelestialBody orbitCenter;
    public final double orbitRadius;
    private double currentAngle;

    public MoonInfo(ResourceLocation texture, double orbitRadius, long orbitDuration, int width, int height, CelestialBody orbitCenter) {
        super(texture, "", 0, 0, width, height, 0xFFFFFF);
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