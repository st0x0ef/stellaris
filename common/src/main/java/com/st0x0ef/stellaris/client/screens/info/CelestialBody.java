package com.st0x0ef.stellaris.client.screens.info;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class CelestialBody {
    public final ResourceLocation texture;
    public final String name;
    public final float width;
    public final float height;
    public final int orbitColor;
    public float x;
    public double y;
    public ResourceKey<Level> dimension;

    public CelestialBody(ResourceLocation texture, String name, float x, float y, float width, float height, int orbitColor, ResourceKey<Level> dimension) {
        this.texture = texture;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.orbitColor = orbitColor;
        this.dimension = dimension;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

}