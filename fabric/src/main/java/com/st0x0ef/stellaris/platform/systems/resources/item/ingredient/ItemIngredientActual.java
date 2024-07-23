package com.st0x0ef.stellaris.platform.systems.resources.item.ingredient;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.platform.systems.resources.util.CodecUtils;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.fabricmc.fabric.impl.recipe.ingredient.CustomIngredientImpl;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.msrandom.multiplatform.annotations.Actual;

import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class ItemIngredientActual {
    @Actual
    public static Ingredient all(Ingredient... ingredients) {
        return DefaultCustomIngredients.all(ingredients);
    }

    @Actual
    public static Ingredient any(Ingredient... ingredients) {
        return DefaultCustomIngredients.any(ingredients);
    }

    @Actual
    public static Ingredient difference(Ingredient base, Ingredient subtracted) {
        return DefaultCustomIngredients.difference(base, subtracted);
    }

    @Actual
    public static Ingredient components(Ingredient base, DataComponentPredicate components) {
        return DefaultCustomIngredients.components(base, components.asPatch());
    }

    @Actual
    public static Ingredient components(ItemStack stack) {
        return DefaultCustomIngredients.components(stack);
    }

    @Actual
    private static MapCodec<Ingredient> getNonEmptyMapCodec() {
        Function<CustomIngredientSerializer<?>, MapCodec<? extends CustomIngredient>> codec = customIngredientSerializer -> customIngredientSerializer.getCodec(false);
        final MapCodec<Ingredient.ItemValue> ITEM_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(ItemStack.SIMPLE_ITEM_CODEC.fieldOf("item").forGetter(arg -> arg.item)).apply(instance, Ingredient.ItemValue::new)
        );

        final MapCodec<Ingredient.TagValue> TAG_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(arg -> arg.tag)).apply(instance, Ingredient.TagValue::new)
        );

        MapCodec<Ingredient.Value> fallbackCodec  = CodecUtils.xor(ITEM_CODEC, TAG_CODEC)
                .xmap(either -> either.map(arg -> arg, arg -> arg), arg -> {
                    if (arg instanceof Ingredient.TagValue ingredient$tagvalue) {
                        return Either.right(ingredient$tagvalue);
                    } else if (arg instanceof Ingredient.ItemValue ingredient$itemvalue) {
                        return Either.left(ingredient$itemvalue);
                    } else {
                        throw new UnsupportedOperationException("This is neither an item value nor a tag value.");
                    }
                });

        var dispatchCodec = CustomIngredientImpl.CODEC.dispatchMap("fabric:type", CustomIngredient::getSerializer, codec);

        return new MapCodec<Either<CustomIngredient, Ingredient.Value>>() {
            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return Stream.concat(dispatchCodec.keys(ops), fallbackCodec.keys(ops)).distinct();
            }

            @Override
            public <T> DataResult<Either<CustomIngredient, Ingredient.Value>> decode(DynamicOps<T> ops, MapLike<T> input) {
                if (input.get("type") != null) {
                    return dispatchCodec.decode(ops, input).map(Either::left);
                } else {
                    return fallbackCodec.decode(ops, input).map(Either::right);
                }
            }

            @Override
            public <T> RecordBuilder<T> encode(Either<CustomIngredient, Ingredient.Value> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                return input.map(
                        dispatched -> dispatchCodec.encode(dispatched, ops, prefix),
                        fallback -> fallbackCodec.encode(fallback, ops, prefix));
            }

            @Override
            public String toString() {
                return "DispatchOrElse[" + dispatchCodec + ", " + fallbackCodec + "]";
            }
        }.xmap(either -> either.map(CustomIngredient::toVanilla, v -> Ingredient.fromValues(Stream.of(v))), ingredient -> {
            Ingredient compoundIngredient;
            if (ingredient.getCustomIngredient() != null && ingredient.getItems().length != 1) {
                // Convert vanilla ingredient to custom CompoundIngredient
                compoundIngredient = DefaultCustomIngredients.any(Stream.of(ingredient.values).map(v -> Ingredient.fromValues(Stream.of(v))).toArray(Ingredient[]::new));
            } else {
                compoundIngredient = ingredient;
            }

            var customIngredient = compoundIngredient.getCustomIngredient();
            if (customIngredient == null) {
                return Either.right(ingredient.values[0]);
            } else {
                return Either.left(customIngredient);
            }
        })
        .validate(ingredient -> {
            if (ingredient.getCustomIngredient() != null && ingredient.values.length == 0) {
                return DataResult.error(() -> "Cannot serialize empty ingredient using the map codec");
            }
            return DataResult.success(ingredient);
        });
    }
}
