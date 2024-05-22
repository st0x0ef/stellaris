package com.st0x0ef.stellaris.client.screens.info;

import net.minecraft.resources.ResourceLocation;

public class CelestialBody {
    public final ResourceLocation texture;
    public final String name;
    public final float width;
    public final float height;
    public final int orbitColor;
    public float x;
    public float y;

    public CelestialBody(ResourceLocation texture, String name, float x, float y, float width, float height, int orbitColor) {
        this.texture = texture;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.orbitColor = orbitColor;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}