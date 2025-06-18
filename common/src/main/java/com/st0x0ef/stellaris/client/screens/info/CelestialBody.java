package com.st0x0ef.stellaris.client.screens.info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.client.screens.etc.Trail;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CelestialBody {

    public final ResourceLocation texture;
    public final String name;
    public final float width;
    public final float height;
    public final int orbitColor;
    public float x;
    public float y;
    public ResourceLocation dimension;
    public String translatable;
    public String id;
    public boolean clickable = true;
    public boolean spaceStation = false;
    public final Trail trail = new Trail();

    public CelestialBody(ResourceLocation texture, String name, float x, float y, float width, float height, int orbitColor, ResourceLocation dimension, String translatable, String id) {
        this(texture, name, x, y, width, height, orbitColor, dimension, translatable, id, true);
    }

    public CelestialBody(ResourceLocation texture, String name, float x, float y, float width, float height, int orbitColor, ResourceLocation dimension, String translatable, String id, boolean clickable) {
        this.texture = texture;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.orbitColor = orbitColor;
        this.dimension = dimension;
        this.translatable = translatable;
        this.id = id;
        this.clickable = clickable;
    }


    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public CelestialBody setSpaceStation(boolean spaceStation) {
        this.spaceStation = spaceStation;
        return this;
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

    public float getWidth() {return width;}

    public static final Codec<CelestialBody> CODEC = RecordCodecBuilder.create(
            instance ->
                    instance
                            .group(
                                    ResourceLocation.CODEC.fieldOf("texture").forGetter(b -> b.texture),
                                    Codec.STRING.fieldOf("name").forGetter(b -> b.name),
                                    Codec.FLOAT.fieldOf("x").forGetter(b -> b.x),
                                    Codec.FLOAT.fieldOf("y").forGetter(b -> b.y),
                                    Codec.FLOAT.fieldOf("width").forGetter(b -> b.width),
                                    Codec.FLOAT.fieldOf("height").forGetter(b -> b.height),
                                    Codec.INT.fieldOf("orbitColor").forGetter(b -> b.orbitColor),
                                    ResourceLocation.CODEC.fieldOf("dimension").forGetter(b -> b.dimension),
                                    Codec.STRING.fieldOf("translatable").forGetter(b -> b.translatable),
                                    Codec.STRING.fieldOf("id").forGetter(b -> b.id),
                                    Codec.BOOL.optionalFieldOf("clickable", true).forGetter(b -> b.clickable)
                            )
                            .apply(instance, CelestialBody::new)
    );

}