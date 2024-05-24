package com.st0x0ef.stellaris.client.screens.info;

import net.minecraft.resources.ResourceLocation;

public class MoonInfo extends CelestialBody {
    public final PlanetInfo orbitCenter;
    private final long orbitalPeriod;
    public final double orbitRadius;
    public double currentAngle;

    public MoonInfo(ResourceLocation texture, String name, double orbitRadius, long orbitalPeriod, int width, int height, PlanetInfo orbitCenter) {
        super(texture, orbitCenter.name + " Moon", 0, 0, width, height, 0xFFFFFF);
        this.orbitRadius = orbitRadius;
        this.orbitalPeriod = orbitalPeriod;
        this.orbitCenter = orbitCenter;
        this.currentAngle = 0;
    }

    public void updateAngle(long currentTime) {
        this.currentAngle = (currentTime % orbitalPeriod) * (2 * Math.PI / orbitalPeriod);
    }

    public void updatePosition() {
        this.x = (float) (orbitCenter.x + orbitRadius * Math.cos(currentAngle));
        this.y = orbitCenter.y + orbitRadius * Math.sin(currentAngle);
    }
}
