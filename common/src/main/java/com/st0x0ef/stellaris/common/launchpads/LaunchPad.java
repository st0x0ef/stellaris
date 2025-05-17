package com.st0x0ef.stellaris.common.launchpads;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public record LaunchPad(
        Vec3 position,
        ResourceKey<Level> dimension,
        String name,
        Boolean isPublic,
        String owner,
        List<String> whitelist
) {

    public static final Codec<LaunchPad> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("position").forGetter(LaunchPad::position),
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(LaunchPad::dimension),
            Codec.STRING.fieldOf("name").forGetter(LaunchPad::name),
            Codec.BOOL.fieldOf("public").forGetter(LaunchPad::isPublic),

            Codec.STRING.fieldOf("owner").forGetter(LaunchPad::name),
            Codec.STRING.listOf().fieldOf("whitelist").forGetter(LaunchPad::whitelist)
    ).apply(instance, LaunchPad::new));

    public static RegistryFriendlyByteBuf toBuffer(LaunchPad launchPad, final RegistryFriendlyByteBuf buffer) {
        buffer.writeVec3(launchPad.position());
        buffer.writeResourceKey(launchPad.dimension());
        buffer.writeUtf(launchPad.name());
        buffer.writeBoolean(launchPad.isPublic());
        buffer.writeUtf(launchPad.owner());
        buffer.writeInt(launchPad.whitelist().size());
        launchPad.whitelist().forEach(buffer::writeUtf);

        return buffer;
    }

    public static LaunchPad readFromBuffer(RegistryFriendlyByteBuf buffer) {
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

        return new LaunchPad(position, dimension, name, isPublic, owner, whitelist);
    }

    public record LaunchPadContainer(List<LaunchPad> launchPads){
        public static final Codec<LaunchPadContainer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LaunchPad.CODEC.listOf().fieldOf("launchpads").forGetter(LaunchPadContainer::launchPads)
        ).apply(instance, LaunchPadContainer::new));

        public static LaunchPadContainer DEFAULT = new LaunchPadContainer(new ArrayList<>());

        public static RegistryFriendlyByteBuf toBuffer(LaunchPadContainer launchPads, final RegistryFriendlyByteBuf buffer) {
            buffer.writeInt(launchPads.launchPads.size());
            launchPads.launchPads.forEach(launchPad -> {
                LaunchPad.toBuffer(launchPad, buffer);
            });

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
            return CODEC
                    .encodeStart(JsonOps.INSTANCE, instance)
                    .result()
                    .orElseThrow(() -> new IllegalStateException("Failed to encode to JSON"));
        }


    }

}
