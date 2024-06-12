package com.st0x0ef.stellaris.client.skys.type;


public class Star {
    public double x, y, z;
    public float size;
    public float r, g, b;

    public Star(double x, double y, double z, float size, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void update(float rotationAngle) {
        double radians = Math.toRadians(rotationAngle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double newX = x * cos - z * sin;
        double newZ = x * sin + z * cos;

        this.x = newX;
        this.z = newZ;
    }
}