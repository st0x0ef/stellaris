package com.st0x0ef.stellaris.client.screens.info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record GalaxyInfo(ResourceLocation texture, String name, String translatable, String id, String centerStar) {

    public Component getTranslatable() {
        return Component.translatable(translatable);
    }

    public static final Codec<GalaxyInfo> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("texture").forGetter(g -> g.texture),
                    Codec.STRING.fieldOf("name").forGetter(g -> g.name),
                    Codec.STRING.fieldOf("translatable").forGetter(g -> g.translatable),
                    Codec.STRING.fieldOf("id").forGetter(g -> g.id),
                    Codec.STRING.fieldOf("centerStar").forGetter(g -> g.centerStar)
            ).apply(instance, GalaxyInfo::new)
    );
}
