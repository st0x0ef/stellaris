package com.st0x0ef.stellaris.client.skys.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record StarType (
        ResourceKey<Level> dimension,
        int count_fancy,
        int count_fast,
        boolean colored,
        WhenType when
) {
    public static final Codec<StarType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("id").forGetter(StarType::dimension),
            Codec.INT.fieldOf("count_fancy").forGetter(StarType::count_fancy),
            Codec.INT.fieldOf("count_fast").forGetter(StarType::count_fast),
            Codec.BOOL.fieldOf("colored").forGetter(StarType::colored),
            Utils.EnumCodec(WhenType.class).fieldOf("shine_time").forGetter(StarType::when)
    ).apply(instance, StarType::new));
}
