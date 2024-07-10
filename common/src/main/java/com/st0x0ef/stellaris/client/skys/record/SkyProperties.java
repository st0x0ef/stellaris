package com.st0x0ef.stellaris.client.skys.record;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public record SkyProperties(
        ResourceKey<Level> id,
        String cloud,
        List<Weather> weather,
        String sunriseColor,
        List<Star> stars,
        List<SkyObject> skyObjects
) {
    public static final Codec<SkyProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("id").forGetter(SkyProperties::id),
            Codec.STRING.fieldOf("cloud").forGetter(SkyProperties::cloud),
            Weather.CODEC.listOf().fieldOf("weather").forGetter(SkyProperties::weather),
            Codec.STRING.fieldOf("sunrise_color").forGetter(SkyProperties::sunriseColor),
            Star.CODEC.listOf().fieldOf("stars").forGetter(SkyProperties::stars),
            SkyObject.CODEC.listOf().fieldOf("sky_objects").forGetter(SkyProperties::skyObjects)
    ).apply(instance, SkyProperties::new));

    public static void toBuffer(List<SkyProperties> properties, FriendlyByteBuf buffer) {
        buffer.writeInt(properties.size());
        properties.forEach(property -> {
            writeResourceKey(buffer, property.id);
            buffer.writeUtf(property.cloud);
            buffer.writeCollection(property.weather, (buf, weather) -> {
                buf.writeBoolean(weather.rain());
                buf.writeBoolean(weather.acidRain());
            });
            buffer.writeUtf(property.sunriseColor);
            buffer.writeCollection(property.stars, (buf, star) -> {
                buf.writeInt(star.count());
                buf.writeBoolean(star.colored());
                buf.writeBoolean(star.allDaysVisible());
            });
            buffer.writeCollection(property.skyObjects, (buf, skyObject) -> {
                buf.writeUtf(skyObject.texture());
                buf.writeBoolean(skyObject.blend());
                buf.writeFloat(skyObject.size());
                buf.writeDouble(skyObject.rotation().x());
                buf.writeDouble(skyObject.rotation().y());
                buf.writeDouble(skyObject.rotation().z());
                buf.writeUtf(skyObject.rotationType());
            });
        });
    }

    public static List<SkyProperties> readFromBuffer(FriendlyByteBuf buffer) {
        List<SkyProperties> properties = new ArrayList<>();
        int count = buffer.readInt();

        for (int i = 0; i < count; i++) {
            List<Weather> weatherList = buffer.readList(buf -> new Weather(buf.readBoolean(), buf.readBoolean()));
            List<Star> starList = buffer.readList(buf -> new Star(buf.readInt(), buf.readBoolean(), buf.readBoolean()));
            List<SkyObject> skyObjectList = buffer.readList(buf -> new SkyObject(
                    buf.readUtf(),
                    buf.readBoolean(),
                    buf.readFloat(),
                    new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                    buf.readUtf()
            ));
            properties.add(new SkyProperties(
                    readResourceKey(buffer),
                    buffer.readUtf(),
                    weatherList,
                    buffer.readUtf(),
                    starList,
                    skyObjectList
            ));
        }

        return properties;
    }

    private static void writeResourceKey(FriendlyByteBuf buffer, ResourceKey<Level> key) {
        buffer.writeResourceLocation(key.location());
    }

    private static ResourceKey<Level> readResourceKey(FriendlyByteBuf buffer) {
        ResourceLocation location = buffer.readResourceLocation();
        return ResourceKey.create(Registries.DIMENSION, location);
    }
}
