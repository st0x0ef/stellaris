package com.st0x0ef.stellaris.client.screens.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record PSystemRecord(
        String name, String translatable, String parent, int starCount, String id,
        List<StarPosition> stars, float centerX, float centerY) {

    public static final Codec<PSystemRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(g -> g.name),
            Codec.STRING.fieldOf("translatable").forGetter(g -> g.translatable),
            Codec.STRING.fieldOf("parent").forGetter(g -> g.parent),
            Codec.INT.fieldOf("starCount").forGetter(g -> g.starCount),
            Codec.STRING.fieldOf("id").forGetter(g -> g.id),
            StarPosition.CODEC.listOf().fieldOf("stars").forGetter(g -> g.stars),
            Codec.FLOAT.fieldOf("centerX").forGetter(g -> g.centerX),
            Codec.FLOAT.fieldOf("centerY").forGetter(g -> g.centerY)
    ).apply(instance, PSystemRecord::new));

    public record StarPosition(String id) {
        public static final Codec<StarPosition> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("id").forGetter(s -> s.id)
        ).apply(i, StarPosition::new));
    }
}
