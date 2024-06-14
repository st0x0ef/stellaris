package com.st0x0ef.stellaris.client.skys.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.skys.type.PlanetCloudType;
import com.st0x0ef.stellaris.client.skys.type.SkyObjectType;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

public record Renderable(
        ResourceKey<Level> dimension,
        PlanetCloudType cloudType,
        boolean weather,
        String sunriseColor,
        int star_count,
        String star_color,
        List<SkyObjectType> skyObject
) {
    public static final Codec<Renderable> CODEC = RecordCodecBuilder.create(renderableInstance -> renderableInstance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("id").forGetter(Renderable::dimension),
            Utils.EnumCodec(PlanetCloudType.class).fieldOf("cloud").forGetter(Renderable::cloudType),
            Codec.BOOL.fieldOf("weather").forGetter(Renderable::weather),
            Codec.STRING.fieldOf("sunrise_color").forGetter(Renderable::sunriseColor),
            Codec.INT.fieldOf("star_count").forGetter(Renderable::star_count),
            Codec.STRING.fieldOf("star_color").forGetter(Renderable::star_color),
            SkyObjectType.CODEC.listOf().fieldOf("sky_object").forGetter(Renderable::skyObject)
    ).apply(renderableInstance, Renderable::new));
}
