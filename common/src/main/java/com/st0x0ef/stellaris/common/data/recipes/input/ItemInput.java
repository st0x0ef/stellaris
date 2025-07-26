package com.st0x0ef.stellaris.common.data.recipes.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ItemInput(Optional<ResourceLocation> item, Optional<TagKey<Item>> tag, int count) {
    public static final Codec<ItemInput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("item").forGetter(ItemInput::item),
            TagKey.codec(BuiltInRegistries.ITEM.key()).optionalFieldOf("tag").forGetter(ItemInput::tag),
            Codec.INT.fieldOf("count").forGetter(ItemInput::count)).apply(instance, ItemInput::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInput> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ItemInput decode(RegistryFriendlyByteBuf buf) {
            return new ItemInput(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ItemInput packet) {
            buf.writeOptional(packet.item, ResourceLocation.STREAM_CODEC);
            buf.writeOptional(packet.tag, TAG_KEY_STREAM_CODEC);
            buf.writeInt(packet.count);
        }
    };

    private static final StreamCodec<FriendlyByteBuf, TagKey<Item>> TAG_KEY_STREAM_CODEC = StreamCodec.of(
            (buf, tagKey) -> buf.writeResourceLocation(tagKey.location()),
            buf -> TagKey.create(Registries.ITEM, buf.readResourceLocation())
    );

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        if (item.isPresent()) {
            items.add(BuiltInRegistries.ITEM.get(item.get()));
        } else if (tag.isPresent()) {
            BuiltInRegistries.ITEM.getTagOrEmpty(tag.get()).forEach(holder -> items.add(holder.value()));
        } else {
            throw new IllegalStateException("ItemInput must have either an item or a tag defined.");
        }

        return items;
    }

    public ItemInput(RegistryFriendlyByteBuf buffer) {
        this(buffer.readOptional(ResourceLocation.STREAM_CODEC), buffer.readOptional(TAG_KEY_STREAM_CODEC), buffer.readInt());
    }

    public String getDisplayName() {
        if (item.isPresent()) {
            return BuiltInRegistries.ITEM.get(item.get()).getDescription().getString();
        } else if (tag.isPresent()) {
            return "#" + tag.get().location().getNamespace() + ":" + tag.get().location().getPath();
        }
        return "Unknown ItemInput";
    }
}
