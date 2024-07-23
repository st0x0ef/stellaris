package com.st0x0ef.stellaris.platform.systems.resources.entity.ingredient.impl;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.platform.systems.resources.ResourceLib;
import com.st0x0ef.stellaris.platform.systems.resources.entity.EntityResource;
import com.st0x0ef.stellaris.platform.systems.resources.entity.ingredient.EntityIngredient;
import com.st0x0ef.stellaris.platform.systems.resources.entity.ingredient.EntityIngredientType;
import com.st0x0ef.stellaris.platform.systems.resources.util.CodecUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BaseEntityIngredient implements EntityIngredient {
    public static final Codec<BaseEntityIngredient> CODEC = CodecUtils.listAndObjectCodec(Value.CODEC).xmap(BaseEntityIngredient::new, BaseEntityIngredient::values);
    public static final MapCodec<BaseEntityIngredient> MAP_CODEC = CODEC.fieldOf("values");
    public static final EntityIngredientType<BaseEntityIngredient> TYPE = new EntityIngredientType<>(new ResourceLocation(ResourceLib.MOD_ID, "base"), MAP_CODEC);

    private final List<Value> values;
    private List<EntityResource> matchingEntitys;

    private BaseEntityIngredient(Value... values) {
        this.values = List.of(values);
    }

    private BaseEntityIngredient(List<Value> values) {
        this.values = values;
    }

    public static BaseEntityIngredient of(EntityResource... resources) {
        List<Value> values = new ArrayList<>();

        for (EntityResource resource : resources) {
            values.add(new ItemValue(resource));
        }

        return new BaseEntityIngredient(values);
    }

    public static BaseEntityIngredient of(TagKey<EntityType<?>> tag) {
        return new BaseEntityIngredient(new TagValue(tag));
    }

    public List<Value> values() {
        return this.values;
    }

    @Override
    public boolean test(EntityResource EntityResource) {
        return false;
    }

    @Override
    public List<EntityResource> getMatchingEntities() {
        if (this.matchingEntitys == null) {
            List<EntityResource> matchingEntitys = new ArrayList<>();

            for (Value value : this.values) {
                matchingEntitys.addAll(value.getItems());
            }

            this.matchingEntitys = matchingEntitys;
        }
        return this.matchingEntitys;
    }

    @Override
    public boolean requiresTesting() {
        return false;
    }

    @Override
    public EntityIngredientType<?> getType() {
        return TYPE;
    }

    public record ItemValue(EntityResource entity) implements Value {
        static final MapCodec<ItemValue> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(EntityResource.SIMPLE_CODEC.fieldOf("entity").forGetter(arg -> arg.entity)).apply(instance, ItemValue::new)
        );

        static final Codec<ItemValue> CODEC = MAP_CODEC.codec();

        public boolean equals(Object object) {
            return object instanceof ItemValue ingredient$itemvalue && ingredient$itemvalue.entity.isOf(this.entity.getType());
        }

        @Override
        public Collection<EntityResource> getItems() {
            return Collections.singleton(this.entity);
        }
    }

    public record TagValue(TagKey<EntityType<?>> tag) implements Value {
        static final MapCodec<TagValue> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(TagKey.codec(Registries.ENTITY_TYPE).fieldOf("tag").forGetter(arg -> arg.tag)).apply(instance, TagValue::new)
        );
        static final Codec<TagValue> CODEC = MAP_CODEC.codec();

        public boolean equals(Object object) {
            return object instanceof TagValue ingredient$tagvalue && ingredient$tagvalue.tag.location().equals(this.tag.location());
        }

        @Override
        public Collection<EntityResource> getItems() {
            List<EntityResource> list = new ArrayList<>();

            for(Holder<EntityType<?>> holder : BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(this.tag)) {
                list.add(EntityResource.of(holder));
            }

            return list;
        }
    }

    public interface Value {
        MapCodec<Value> MAP_CODEC = CodecUtils.xor(ItemValue.MAP_CODEC, TagValue.MAP_CODEC)
                .xmap(either -> either.map(arg -> arg, arg -> arg), arg -> {
                    if (arg instanceof TagValue ingredient$tagvalue) {
                        return Either.right(ingredient$tagvalue);
                    } else if (arg instanceof ItemValue ingredient$itemvalue) {
                        return Either.left(ingredient$itemvalue);
                    } else {
                        throw new UnsupportedOperationException("This is neither an item value nor a tag value.");
                    }
                });
        Codec<Value> CODEC = MAP_CODEC.codec();

        Collection<EntityResource> getItems();
    }
}
