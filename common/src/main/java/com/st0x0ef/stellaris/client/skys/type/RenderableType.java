package com.st0x0ef.stellaris.client.skys.type;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

public class RenderableType {
    private final ResourceKey<Level> dimension;
    private final PlanetCloudType cloudType;
    private final boolean weather;
    private final String sunriseColor;
    private final int starCount;
    private final String starColor;
    private final List<SkyObjectType> skyObject;

    public RenderableType(ResourceKey<Level> dimension, PlanetCloudType cloudType, boolean weather, String sunriseColor, int starCount, String starColor, List<SkyObjectType> skyObject) {
        this.dimension = dimension;
        this.cloudType = cloudType;
        this.weather = weather;
        this.sunriseColor = sunriseColor;
        this.starCount = starCount;
        this.starColor = starColor;
        this.skyObject = skyObject;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public PlanetCloudType getCloudType() {
        return cloudType;
    }

    public boolean isWeather() {
        return weather;
    }

    public String getSunriseColor() {
        return sunriseColor;
    }

    public int getStarCount() {
        return starCount;
    }

    public String getStarColor() {
        return starColor;
    }

    public List<SkyObjectType> getSkyObject() {
        return skyObject;
    }
}
