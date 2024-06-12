package com.st0x0ef.stellaris.client.skys.type;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

public class RenderInfo {
    private final ResourceKey<Level> dimensionId;
    private final PlanetCloudType cloudType;
    private final boolean weather;
    private final int sunriseColor;
    private final List<StarType> star;
    private final List<SkyObjectType> skyObject;

    public RenderInfo(ResourceKey<Level> dimensionId, PlanetCloudType cloudType, boolean weather, int sunriseColor, List<StarType> star, List<SkyObjectType> skyObject) {
        this.dimensionId = dimensionId;
        this.cloudType = cloudType;
        this.weather = weather;
        this.sunriseColor = sunriseColor;
        this.star = star;
        this.skyObject = skyObject;
    }

    public ResourceKey<Level> getDimensionId() {
        return dimensionId;
    }

    public PlanetCloudType getCloudType() {
        return cloudType;
    }

    public boolean isWeather() {
        return weather;
    }

    public int getSunriseColor() {
        return sunriseColor;
    }

    public List<StarType> getStar() {
        return star;
    }

    public List<SkyObjectType> getSkyObject() {
        return skyObject;
    }

    public Planet getPlanet(ResourceKey<Level> key) {
        return PlanetUtil.getPlanet(key);
    }
}
