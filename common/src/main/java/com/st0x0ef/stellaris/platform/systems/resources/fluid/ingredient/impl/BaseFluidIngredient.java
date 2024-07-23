package com.st0x0ef.stellaris.platform.systems.resources.fluid.ingredient.impl;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.platform.systems.resources.ResourceLib;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.ingredient.FluidIngredient;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.ingredient.FluidIngredientType;
import com.st0x0ef.stellaris.platform.systems.resources.util.CodecUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BaseFluidIngredient implements FluidIngredient {
    public static final Codec<BaseFluidIngredient> CODEC = CodecUtils.listAndObjectCodec(Value.CODEC).xmap(BaseFluidIngredient::new, BaseFluidIngredient::values);
    public static final MapCodec<BaseFluidIngredient> MAP_CODEC = CODEC.fieldOf("values");
    public static final FluidIngredientType<BaseFluidIngredient> TYPE = new FluidIngredientType<>(new ResourceLocation(ResourceLib.MOD_ID, "base"), MAP_CODEC);

    private final List<Value> values;
    private List<FluidResource> matchingFluids;

    private BaseFluidIngredient(Value... values) {
        this.values = List.of(values);
    }

    private BaseFluidIngredient(List<Value> values) {
        this.values = values;
    }

    public static BaseFluidIngredient of(FluidResource... resources) {
        List<Value> values = new ArrayList<>();

        for (FluidResource resource : resources) {
            values.add(new ItemValue(resource));
        }

        return new BaseFluidIngredient(values);
    }

    public static BaseFluidIngredient of(TagKey<Fluid> tag) {
        return new BaseFluidIngredient(new TagValue(tag));
    }

    public List<Value> values() {
        return this.values;
    }

    @Override
    public boolean test(FluidResource fluidResource) {
        return false;
    }

    @Override
    public List<FluidResource> getMatchingFluids() {
        if (this.matchingFluids == null) {
            List<FluidResource> matchingFluids = new ArrayList<>();

            for (Value value : this.values) {
                matchingFluids.addAll(value.getItems());
            }

            this.matchingFluids = matchingFluids;
        }
        return this.matchingFluids;
    }

    @Override
    public boolean requiresTesting() {
        return false;
    }

    @Override
    public FluidIngredientType<?> getType() {
        return TYPE;
    }

    public record ItemValue(FluidResource fluid) implements Value {
        static final MapCodec<ItemValue> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(FluidResource.SIMPLE_CODEC.fieldOf("fluid").forGetter(arg -> arg.fluid)).apply(instance, ItemValue::new)
        );

        static final Codec<ItemValue> CODEC = MAP_CODEC.codec();

        public boolean equals(Object object) {
            return object instanceof ItemValue ingredient$itemvalue && ingredient$itemvalue.fluid.isOf(this.fluid.getType());
        }

        @Override
        public Collection<FluidResource> getItems() {
            return Collections.singleton(this.fluid);
        }
    }

    public record TagValue(TagKey<Fluid> tag) implements Value {
        static final MapCodec<TagValue> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(TagKey.codec(Registries.FLUID).fieldOf("tag").forGetter(arg -> arg.tag)).apply(instance, TagValue::new)
        );
        static final Codec<TagValue> CODEC = MAP_CODEC.codec();

        public boolean equals(Object object) {
            return object instanceof TagValue ingredient$tagvalue && ingredient$tagvalue.tag.location().equals(this.tag.location());
        }

        @Override
        public Collection<FluidResource> getItems() {
            List<FluidResource> list = new ArrayList<>();

            for(Holder<Fluid> holder : BuiltInRegistries.FLUID.getTagOrEmpty(this.tag)) {
                list.add(FluidResource.of(holder));
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

        Collection<FluidResource> getItems();
    }
}
