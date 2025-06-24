package com.st0x0ef.stellaris.common.launchpads;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public record LaunchPad(
        int id,
        Vec3 position,
        ResourceKey<Level> dimension,
        String name,
        Boolean isPublic,
        String owner,
        List<String> whitelist
) {

    public static final Codec<LaunchPad> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(launchPad -> (int) launchPad.position().x),
            Vec3.CODEC.fieldOf("position").forGetter(LaunchPad::position),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(LaunchPad::dimension),
            Codec.STRING.fieldOf("name").forGetter(LaunchPad::name),
            Codec.BOOL.fieldOf("public").forGetter(LaunchPad::isPublic),

            Codec.STRING.fieldOf("owner").forGetter(LaunchPad::name),
            Codec.STRING.listOf().fieldOf("whitelist").forGetter(LaunchPad::whitelist)
    ).apply(instance, LaunchPad::new));

    @Override
    public String toString() {
        return "LaunchPad : " +
                "id=" + id +
                ", position=" + position +
                ", dimension=" + dimension +
                ", name='" + name + '\'' +
                ", isPublic=" + isPublic +
                ", owner='" + owner + '\'' +
                ", whitelist=" + whitelist +
                '}';
    }

    public static RegistryFriendlyByteBuf toBuffer(LaunchPad launchPad, final RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(launchPad.id);
        buffer.writeVec3(launchPad.position());
        buffer.writeResourceKey(launchPad.dimension());
        buffer.writeUtf(launchPad.name());
        buffer.writeBoolean(launchPad.isPublic());
        buffer.writeUtf(launchPad.owner());
        buffer.writeInt(launchPad.whitelist().size());
        launchPad.whitelist().forEach(buffer::writeUtf);

        return buffer;
    }

    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);

        JsonArray pos = new JsonArray();
        pos.add(position.x);
        pos.add(position.y);
        pos.add(position.z);

        json.add("position", pos);
        json.addProperty("dimension", dimension.location().toString());

        json.addProperty("name", name);
        json.addProperty("public", isPublic);
        json.addProperty("owner", owner);

        JsonArray whitelist = new JsonArray();
        for (String s : this.whitelist) {
            whitelist.add(s);
        }
        json.add("whitelist", whitelist);
        return json;
    }

    public static JsonElement toJson(LaunchPad instance) {
        return CODEC
                .encodeStart(JsonOps.INSTANCE, instance)
                .result()
                .orElseThrow(() -> new IllegalStateException("Failed to encode to JSON"));
    }


    public static LaunchPad readFromBuffer(RegistryFriendlyByteBuf buffer) {
        var id = buffer.readInt();

        var position = buffer.readVec3();
        var dimension = buffer.readResourceKey(Registries.DIMENSION);
        var name = buffer.readUtf();
        var isPublic = buffer.readBoolean();

        var owner = buffer.readUtf();
        var whitelistSize = buffer.readInt();

        List<String> whitelist = new ArrayList<>();

        for (int i = 0; i < whitelistSize; i++) {
            whitelist.add(buffer.readUtf());
        }

        return new LaunchPad(id, position, dimension, name, isPublic, owner, whitelist);
    }

    public record LaunchPadContainer(List<LaunchPad> launchPads){
        public static final Codec<LaunchPadContainer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LaunchPad.CODEC.listOf().fieldOf("launchpads").forGetter(LaunchPadContainer::launchPads)
        ).apply(instance, LaunchPadContainer::new));

        public static LaunchPadContainer DEFAULT = new LaunchPadContainer(new ArrayList<>());

        public static RegistryFriendlyByteBuf toBuffer(LaunchPadContainer launchPads, final RegistryFriendlyByteBuf buffer) {
            buffer.writeInt(launchPads.launchPads.size());
            launchPads.launchPads.forEach(launchPad -> LaunchPad.toBuffer(launchPad, buffer));

            return buffer;
        }

        public static LaunchPadContainer readFromBuffer(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readInt();
            List<LaunchPad> launchPads = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                launchPads.add(LaunchPad.readFromBuffer(buffer));
            }
            return new LaunchPadContainer(launchPads);
        }

        public static JsonElement toJson(LaunchPadContainer instance) {
            JsonObject json = new JsonObject();
            JsonArray launchPadArray = new JsonArray();

            for (LaunchPad launchPad : instance.launchPads) {
                launchPadArray.add(launchPad.toJson());
            }

            json.add("launchpads", launchPadArray);

            return json;
        }


    }

}
