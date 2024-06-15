package com.st0x0ef.stellaris.client.skys.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record StarTypeRecord(
        int count,
        boolean color,
        boolean allDaysVisible
) {
    public static final Codec<StarTypeRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(StarTypeRecord::count),
            Codec.BOOL.fieldOf("colored").forGetter(StarTypeRecord::color),
            Codec.BOOL.fieldOf("allDaysVisible").forGetter(StarTypeRecord::allDaysVisible)
    ).apply(instance, StarTypeRecord::new));
}
