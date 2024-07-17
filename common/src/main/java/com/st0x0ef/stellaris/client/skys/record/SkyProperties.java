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
        Boolean cloud,
        Boolean fog,
        CustomVanillaObject customVanillaObject,
        List<Weather> weather,
        int sunriseColor,
        Star stars,
        List<SkyObject> skyObjects
) {
    public static final Codec<SkyProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("id").forGetter(SkyProperties::id),
            Codec.BOOL.fieldOf("cloud").forGetter(SkyProperties::cloud),
            Codec.BOOL.fieldOf("fog").forGetter(SkyProperties::fog),
            CustomVanillaObject.CODEC.fieldOf("custom_vanilla_objects").forGetter(SkyProperties::customVanillaObject),

            Weather.CODEC.listOf().fieldOf("weather").forGetter(SkyProperties::weather),
            Codec.INT.fieldOf("sunrise_color").forGetter(SkyProperties::sunriseColor),
            Star.CODEC.fieldOf("stars").forGetter(SkyProperties::stars),
            SkyObject.CODEC.listOf().fieldOf("sky_objects").forGetter(SkyProperties::skyObjects)
    ).apply(instance, SkyProperties::new));

    public static void toBuffer(List<SkyProperties> properties, FriendlyByteBuf buffer) {
        buffer.writeInt(properties.size());
        properties.forEach(property -> {
            writeResourceKey(buffer, property.id);
            buffer.writeBoolean(property.cloud);
            buffer.writeBoolean(property.fog);
            //TODO Add buffer for CustomVanillaObject
            buffer.writeCollection(property.weather, (buf, weather) -> {
                buf.writeBoolean(weather.rain());
                buf.writeBoolean(weather.acidRain());
            });
            buffer.writeInt(property.sunriseColor);

            buffer.writeInt(property.stars.count());
            buffer.writeBoolean(property.stars.colored());
            buffer.writeBoolean(property.stars.allDaysVisible());


            buffer.writeCollection(property.skyObjects, (buf, skyObject) -> {
                buf.writeResourceLocation(skyObject.texture());
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
            List<SkyObject> skyObjectList = buffer.readList(buf -> new SkyObject(
                    buf.readResourceLocation(),
                    buf.readBoolean(),
                    buf.readFloat(),
                    new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                    buf.readUtf()
            ));
            properties.add(new SkyProperties(
                    readResourceKey(buffer),
                    buffer.readBoolean(),
                    buffer.readBoolean(),
                    new CustomVanillaObject(buffer.readBoolean(), buffer.readResourceLocation(),buffer.readBoolean(), buffer.readResourceLocation()),
                    weatherList,
                    buffer.readInt(),
                    new Star(buffer.readInt(), buffer.readBoolean(), buffer.readBoolean()),
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
