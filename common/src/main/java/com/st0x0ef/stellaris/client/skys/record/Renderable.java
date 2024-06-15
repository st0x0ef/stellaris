package com.st0x0ef.stellaris.client.skys.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.skys.type.PlanetCloudType;
import com.st0x0ef.stellaris.client.skys.type.SkyObjectType;
import com.st0x0ef.stellaris.client.skys.record.StarTypeRecord;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

public record Renderable(
        ResourceKey<Level> dimension,
        String cloudType,
        boolean weather,
        String sunriseColor,
        List<StarTypeRecord> star,
        List<SkyObjectType> skyObject
) {
    public static final Codec<Renderable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("id").forGetter(Renderable::dimension),
            Codec.STRING.fieldOf("cloud").forGetter(Renderable::cloudType),
            Codec.BOOL.fieldOf("weather").forGetter(Renderable::weather),
            Codec.STRING.fieldOf("sunrise_color").forGetter(Renderable::sunriseColor),
            StarTypeRecord.CODEC.listOf().fieldOf("star").forGetter(Renderable::star),
            SkyObjectType.CODEC.listOf().fieldOf("sky_object").forGetter(Renderable::skyObject)
    ).apply(instance, Renderable::new));
}
