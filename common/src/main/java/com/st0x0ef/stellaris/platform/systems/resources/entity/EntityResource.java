package com.st0x0ef.stellaris.platform.systems.resources.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.platform.systems.resources.ResourceComponent;
import com.st0x0ef.stellaris.platform.systems.resources.ResourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public final class EntityResource extends ResourceComponent {
    public static EntityResource BLANK = EntityResource.of((EntityType<?>) null);

    public static final Codec<EntityResource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(EntityResource::getType),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(EntityResource::getDataPatch)
    ).apply(instance, EntityResource::of));

    public static final Codec<EntityResource> SIMPLE_CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().xmap(EntityResource::of, EntityResource::getType);

    public static final MapCodec<EntityResource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(EntityResource::getType),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(EntityResource::getDataPatch)
    ).apply(instance, EntityResource::of));

    public static final StreamCodec<RegistryFriendlyByteBuf, EntityResource> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.ENTITY_TYPE),
            EntityResource::toHolder,
            DataComponentPatch.STREAM_CODEC,
            EntityResource::getDataPatch,
            EntityResource::of);

    public static EntityResource of(EntityType<?> type) {
        return of(type, DataComponentPatch.EMPTY);
    }

    public static EntityResource of(Holder<EntityType<?>> type) {
        return of(type.value(), DataComponentPatch.EMPTY);
    }

    public static EntityResource of(EntityType<?> type, DataComponentPatch patch) {
        return new EntityResource(type, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
    }

    public static EntityResource of(Holder<EntityType<?>> type, DataComponentPatch patch) {
        return of(type.value(), patch);
    }

    private final EntityType<?> type;

    private EntityResource(@Nullable EntityType<?> type, PatchedDataComponentMap components) {
        super(components);
        this.type = type;
    }

    public boolean isOf(EntityType<?> type) {
        return this.type == type;
    }

    @Override
    public boolean isBlank() {
        return type == null;
    }

    @Nullable
    public EntityType<?> getType() {
        return type;
    }

    public <D> EntityResource set(DataComponentType<D> type, D value) {
        PatchedDataComponentMap newComponents = new PatchedDataComponentMap(components);
        newComponents.set(type, value);
        return new EntityResource(this.type, newComponents);
    }

    public EntityResource modify(DataComponentPatch patch) {
        PatchedDataComponentMap newComponents = new PatchedDataComponentMap(components);
        newComponents.applyPatch(patch);
        return new EntityResource(type, newComponents);
    }

    public ResourceStack<EntityResource> toStack(long amount) {
        return new ResourceStack<>(this, amount);
    }

    public Holder<EntityType<?>> toHolder() {
        return type.builtInRegistryHolder();
    }
}
