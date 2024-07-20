package com.st0x0ef.stellaris.neoforge.systems.data.impl;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.common.systems.data.DataManager;
import com.st0x0ef.stellaris.common.systems.data.DataManagerBuilder;
import com.st0x0ef.stellaris.neoforge.systems.data.sync.DataSyncSerializer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DataManagerBuilderImpl<T> implements DataManagerBuilder<T> {
    private final DeferredRegister<AttachmentType<?>> registry;
    private final DeferredRegister<DataComponentType<?>> componentRegistry;
    private final DeferredRegister<DataSyncSerializer<?>> serializerRegistry;
    private final AttachmentType.Builder<T> builder;
    private final Supplier<T> factory;

    private Codec<T> codec;
    private StreamCodec<? super RegistryFriendlyByteBuf, T> clientCodec;
    private boolean registerComponentType;
    private boolean syncToClient;

    public DataManagerBuilderImpl(DeferredRegister<AttachmentType<?>> registry, DeferredRegister<DataComponentType<?>> componentRegistry, DeferredRegister<DataSyncSerializer<?>> serializerRegistry, Supplier<T> factory) {
        this.registry = registry;
        this.componentRegistry = componentRegistry;
        this.serializerRegistry = serializerRegistry;
        this.builder = AttachmentType.builder(factory);
        this.factory = factory;
    }

    @Override
    public DataManagerBuilder<T> serialize(Codec<T> codec) {
        builder.serialize(codec);
        this.codec = codec;
        return this;
    }

    @Override
    public DataManagerBuilder<T> copyOnDeath() {
        builder.copyOnDeath();
        return this;
    }

    @Override
    public DataManagerBuilder<T> networkSerializer(StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        this.clientCodec = codec;
        this.syncToClient = true;
        return this;
    }

    @Override
    public DataManagerBuilder<T> networkSerializer() {
        this.syncToClient = true;
        return this;
    }

    @Override
    public DataManagerBuilder<T> withDataComponent() {
        this.registerComponentType = true;
        return this;
    }

    @Override
    public DataManager<T> buildAndRegister(String name) {
        var type = registry.register(name, builder::build);
        Supplier<DataSyncSerializer<T>> serializer = null;
        if (syncToClient) {
            if (clientCodec == null) {
                if (codec == null) {
                    throw new IllegalStateException("No codec or network codec provided for network synchronization");
                } else {
                    clientCodec = ByteBufCodecs.fromCodecWithRegistries(codec);
                }
            }
            serializer = serializerRegistry.register(name, () -> DataSyncSerializer.create(type, clientCodec));
        }
        Supplier<DataComponentType<T>> component = null;
        if (registerComponentType) {
            DataComponentType.Builder<T> componentBuilder = DataComponentType.<T>builder().persistent(codec).networkSynchronized(clientCodec);
            component = componentRegistry.register(name, componentBuilder::build);
        }
        return new DataManagerImpl<>(type, component, serializer, factory);
    }
}