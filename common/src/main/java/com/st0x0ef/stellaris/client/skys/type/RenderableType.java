package com.st0x0ef.stellaris.client.skys.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.skys.record.StarTypeRecord;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

public class RenderableType {
    private final ResourceKey<Level> dimension;
    private final String cloudType;
    private final boolean weather;
    private final String sunriseColor;
    private final int starCount;
    private final boolean starColor;
    private final boolean allDaysVisible;
    private final List<SkyObjectType> skyObject;

    public RenderableType(ResourceKey<Level> dimension, String cloudType, boolean weather, String sunriseColor, StarTypeRecord starType, List<SkyObjectType> skyObject) {
        this.dimension = dimension;
        this.cloudType = cloudType;
        this.weather = weather;
        this.sunriseColor = sunriseColor;
        this.starCount = starType.count();
        this.starColor = starType.color();
        this.allDaysVisible = starType.allDaysVisible();
        this.skyObject = skyObject;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public String getCloudType() {
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

    public boolean getStarColor() {
        return starColor;
    }

    public boolean isAllDaysVisible() {
        return allDaysVisible;
    }

    public List<SkyObjectType> getSkyObject() {
        return skyObject;
    }

    public static final Codec<RenderableType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ResourceKey.codec(Registries.DIMENSION).fieldOf("id").forGetter(RenderableType::getDimension),
                    Codec.STRING.fieldOf("cloud").forGetter(RenderableType::getCloudType),
                    Codec.BOOL.fieldOf("weather").forGetter(RenderableType::isWeather),
                    Codec.STRING.fieldOf("sunrise_color").forGetter(RenderableType::getSunriseColor),
                    StarTypeRecord.CODEC.fieldOf("star").forGetter(rt -> new StarTypeRecord(rt.getStarCount(), rt.getStarColor(), rt.isAllDaysVisible())),
                    SkyObjectType.CODEC.listOf().fieldOf("sky_object").forGetter(RenderableType::getSkyObject)
            ).apply(instance, (dimension, cloudType, weather, sunriseColor, starType, skyObject) ->
                    new RenderableType(dimension, cloudType, weather, sunriseColor, starType, skyObject))
    );
}
