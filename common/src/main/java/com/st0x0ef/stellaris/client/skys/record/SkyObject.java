package com.st0x0ef.stellaris.client.skys.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record SkyObject(ResourceLocation texture, boolean blend, float size, Vec3 rotation, String rotationType) {
    public static final Codec<SkyObject> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(SkyObject::texture),
            Codec.BOOL.fieldOf("blend").forGetter(SkyObject::blend),
            Codec.FLOAT.fieldOf("size").forGetter(SkyObject::size),
            Vec3.CODEC.fieldOf("rotation").forGetter(SkyObject::rotation),
            Codec.STRING.fieldOf("rotation_type").forGetter(SkyObject::rotationType)
    ).apply(instance, SkyObject::new));
}
