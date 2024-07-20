package com.st0x0ef.stellaris.common.systems.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.common.systems.resources.fluid.util.FluidAmounts;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.function.UnaryOperator;

public record ResourceStack<T extends Resource>(T resource, long amount) {
    public static Codec<ResourceStack<FluidResource>> FLUID_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FluidResource.MAP_CODEC.forGetter(ResourceStack::resource),
            Codec.LONG.fieldOf("amount").forGetter(ResourceStack::amount)
    ).apply(instance, ResourceStack::new));

    public static Codec<ResourceStack<FluidResource>> FLUID_MB_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FluidResource.MAP_CODEC.forGetter(ResourceStack::resource),
            Codec.LONG.fieldOf("millibuckets").forGetter(stack -> FluidAmounts.toMillibuckets(stack.amount))
    ).apply(instance, (resource, amount) -> new ResourceStack<>(resource, FluidAmounts.toPlatformAmount(amount))));

    public static Codec<ResourceStack<ItemResource>> ITEM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemResource.MAP_CODEC.forGetter(ResourceStack::resource),
            Codec.LONG.fieldOf("amount").forGetter(ResourceStack::amount)
    ).apply(instance, ResourceStack::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceStack<FluidResource>> FLUID_STREAM_CODEC = StreamCodec.composite(FluidResource.STREAM_CODEC, ResourceStack::resource, ByteBufCodecs.VAR_LONG, ResourceStack::amount, ResourceStack::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceStack<ItemResource>> ITEM_STREAM_CODEC = StreamCodec.composite(ItemResource.STREAM_CODEC, ResourceStack::resource, ByteBufCodecs.VAR_LONG, ResourceStack::amount, ResourceStack::new);

    public static final ResourceStack<FluidResource> EMPTY_FLUID = new ResourceStack<>(FluidResource.BLANK, 0);
    public static final ResourceStack<ItemResource> EMPTY_ITEM = new ResourceStack<>(ItemResource.BLANK, 0);

    public static ItemStack toItemStack(ResourceStack<ItemResource> stack) {
        return stack.resource().toStack((int) stack.amount());
    }

    public ResourceStack<T> grow(long amount) {
        return new ResourceStack<>(resource, this.amount + amount);
    }

    public ResourceStack<T> shrink(long amount) {
        return new ResourceStack<>(resource, this.amount - amount);
    }

    public ResourceStack<T> withCount(long amount) {
        return new ResourceStack<>(resource, amount);
    }

    public ResourceStack<T> with(UnaryOperator<T> operator) {
        return new ResourceStack<>(operator.apply(resource), amount);
    }

    public boolean isEmpty() {
        return amount <= 0 || resource.isBlank();
    }

    @Override
    public long amount() {
        return isEmpty() ? 0 : amount;
    }
}
