package com.st0x0ef.stellaris.platform.systems.resources.entity.ingredient;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.platform.systems.resources.entity.EntityResource;
import com.st0x0ef.stellaris.platform.systems.resources.entity.ingredient.impl.*;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public interface EntityIngredient extends Predicate<EntityResource> {
    Codec<EntityIngredient> TYPE_CODEC = EntityIngredientRegistry.TYPE_CODEC.dispatch(EntityIngredient::getType, EntityIngredientType::codec);
    Codec<EntityIngredient> CODEC = Codec.either(BaseEntityIngredient.CODEC, TYPE_CODEC).xmap(either -> either.map(l -> l, r -> r), ingredient -> ingredient instanceof BaseEntityIngredient ? Either.left((BaseEntityIngredient) ingredient) : Either.right(ingredient));
    MapCodec<EntityIngredient> MAP_CODEC = EntityIngredientRegistry.TYPE_CODEC.dispatchMap(EntityIngredient::getType, EntityIngredientType::codec);

    @SuppressWarnings("unchecked")
    StreamCodec<RegistryFriendlyByteBuf, EntityIngredient> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf regByteBuf, EntityIngredient ingredient) {
            EntityIngredientRegistry.STREAM_CODEC.encode(regByteBuf, ingredient.getType());
            ((StreamCodec<RegistryFriendlyByteBuf, EntityIngredient>) ingredient.getType().streamCodec()).encode(regByteBuf, ingredient);
        }

        @Override
        public @NotNull EntityIngredient decode(RegistryFriendlyByteBuf regByteBuf) {
            EntityIngredientType<?> EntityIngredientType = EntityIngredientRegistry.STREAM_CODEC.decode(regByteBuf);
            return EntityIngredientType.streamCodec().decode(regByteBuf);
        }
    };

    static EntityIngredient of(EntityResource... stacks) {
        return BaseEntityIngredient.of(stacks);
    }

    static EntityIngredient of(TagKey<EntityType<?>> tag) {
        return BaseEntityIngredient.of(tag);
    }

    static EntityIngredient all(EntityIngredient... ingredients) {
        return new AllMatchEntityIngredient(Arrays.asList(ingredients));
    }

    static EntityIngredient any(EntityIngredient... ingredients) {
        return new AnyMatchEntityIngredient(Arrays.asList(ingredients));
    }

    static EntityIngredient difference(EntityIngredient minuend, EntityIngredient subtrahend) {
        return new DifferenceEntityIngredient(minuend, subtrahend);
    }

    static EntityIngredient components(EntityIngredient base, DataComponentPredicate components) {
        return new ComponentEntityIngredient(base, components);
    }

    static SizedEntityIngredient sized(EntityIngredient ingredient, long size) {
        return new SizedEntityIngredient(ingredient, size);
    }

    boolean test(EntityResource EntityResource);

    List<EntityResource> getMatchingEntities();

    boolean requiresTesting();

    EntityIngredientType<?> getType();
}
