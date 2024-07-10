package com.st0x0ef.stellaris.client.skys.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Weather(boolean rain, boolean acidRain) {
    public static final Codec<Weather> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("rain").forGetter(Weather::rain),
            Codec.BOOL.fieldOf("acid_rain").forGetter(Weather::acidRain)
    ).apply(instance, Weather::new));
}
