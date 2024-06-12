package com.st0x0ef.stellaris.client.skys.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record SkyObjectType (
        ResourceLocation texture,
        boolean blend,
        float size,
        Vec3 objectRotation,
        RotationType rotationType

) {
    public static final Codec<SkyObjectType> CODEC = RecordCodecBuilder.create(skyObject -> skyObject.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(SkyObjectType::texture),
            Codec.BOOL.fieldOf("blend").forGetter(SkyObjectType::blend),
            Codec.FLOAT.fieldOf("size").forGetter(SkyObjectType::size),
            Vec3.CODEC.fieldOf("rotation").forGetter(SkyObjectType::objectRotation),
            Utils.EnumCodec(RotationType.class).fieldOf("rotation_type").forGetter(SkyObjectType::rotationType)
    ).apply(skyObject, SkyObjectType::new));
}