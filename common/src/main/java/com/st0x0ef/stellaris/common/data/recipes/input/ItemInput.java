package com.st0x0ef.stellaris.common.data.recipes.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public record ItemInput(ResourceLocation item, int count) {
    public static final Codec<ItemInput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("item").forGetter(ItemInput::item),
            Codec.INT.fieldOf("count").forGetter(ItemInput::count)).apply(instance, ItemInput::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInput> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ItemInput decode(RegistryFriendlyByteBuf buf) {
            return new ItemInput(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ItemInput packet) {
            buf.writeResourceLocation(packet.item);
            buf.writeInt(packet.count);
        }
    };

    public Item getItem() {
        return BuiltInRegistries.ITEM.get(item);
    }

    public ItemInput(RegistryFriendlyByteBuf buffer) {
        this(buffer.readResourceLocation(), buffer.readInt());
    }
}
