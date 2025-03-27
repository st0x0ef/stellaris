package com.st0x0ef.stellaris.client.screens.info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GalaxyInfo {
    public final ResourceLocation texture;
    public final String name;
    public final String translatable;
    public final String id;

    public GalaxyInfo(ResourceLocation texture, String name, String translatable, String id) {
        this.texture = texture;
        this.name = name;
        this.translatable = translatable;
        this.id = id;
    }

    public Component getTranslatable() {
        return Component.translatable(translatable);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public static final Codec<GalaxyInfo> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("texture").forGetter(g -> g.texture),
                    Codec.STRING.fieldOf("name").forGetter(g -> g.name),
                    Codec.STRING.fieldOf("translatable").forGetter(g -> g.translatable),
                    Codec.STRING.fieldOf("id").forGetter(g -> g.id)
            ).apply(instance, GalaxyInfo::new)
    );
}
