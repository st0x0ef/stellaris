package com.st0x0ef.stellaris.client.skys.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Star(int count, boolean colored, boolean allDaysVisible) {
    public static final Codec<Star> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("count").forGetter(Star::count),
            Codec.BOOL.fieldOf("colored").forGetter(Star::colored),
            Codec.BOOL.fieldOf("all_days_visible").forGetter(Star::allDaysVisible)
    ).apply(instance, Star::new));
}
