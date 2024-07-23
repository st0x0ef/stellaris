package com.st0x0ef.stellaris.platform.systems.core.item.input;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.item.input.consumers.SizedConsumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Function;
import java.util.stream.Stream;

public record ItemInput(Ingredient ingredient, ItemConsumer consumer, Stream<ItemStack> stacks) {
    public static final MapCodec<ItemInput> BASE_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ItemInput::ingredient),
            ItemConsumer.CODEC.fieldOf("consume").forGetter(ItemInput::consumer)
    ).apply(instance, ItemInput::new));

    public static final Codec<ItemInput> CODEC = Codec.either(BASE_CODEC.codec(), Ingredient.CODEC).xmap(
            a -> a.map(Function.identity(), ingredient -> new ItemInput(ingredient, SizedConsumer.DEFAULT)),
            Either::left
    );

    public ItemInput(Ingredient ingredient, ItemConsumer consumer) {
        this(ingredient, consumer, Stream.of(ingredient.getItems()).map(consumer::modifyDisplay));
    }

    public boolean test(ItemStack stack, ItemContext context) {
        return ingredient.test(stack) && consumer.test(stack, context);
    }

    public void consume(ItemStack stack, ItemContext context) {
        consumer.consume(stack, context);
    }
}
