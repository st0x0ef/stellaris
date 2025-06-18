package com.st0x0ef.stellaris.client.screens.etc;

import com.st0x0ef.stellaris.client.screens.info.CelestialBody;

public class StarMovement {
    public CelestialBody body;
    public double mass;
    public double vx = 0, vy = 0;
    public double ax = 0, ay = 0;
    public boolean initialized = false;

    public StarMovement(CelestialBody body, double mass) {
        this.body = body;
        this.mass = mass;
    }
}
