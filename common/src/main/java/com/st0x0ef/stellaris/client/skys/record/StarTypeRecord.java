package com.st0x0ef.stellaris.client.skys.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.skys.type.StarType;

public record StarTypeRecord(int count, String color, boolean allDaysVisible) {
    public static final Codec<StarTypeRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(StarType::count),
            Codec.STRING.fieldOf("color").forGetter(StarType::color),
            Codec.BOOL.fieldOf("all_days_visible").forGetter(StarType::allDaysVisible)
    ).apply(instance, StarType::new));
}
