package com.st0x0ef.stellaris.platform.systems.data.impl;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.platform.systems.data.DataManager;
import com.st0x0ef.stellaris.platform.systems.data.DataManagerBuilder;
import com.st0x0ef.stellaris.platform.systems.data.FabricDataLib;
import com.st0x0ef.stellaris.platform.systems.data.sync.DataSyncSerializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class DataManagerBuilderImpl<T> implements DataManagerBuilder<T> {
    private final AttachmentRegistry.Builder<T> builder;
    private final String modid;

    private StreamCodec<? super RegistryFriendlyByteBuf, T> clientCodec;
    private Codec<T> codec;
    private boolean registerComponentType;
    private boolean syncToClient;

    public DataManagerBuilderImpl(String modid, Supplier<T> factory) {
        this.builder = AttachmentRegistry.builder();
        this.modid = modid;
        this.builder.initializer(factory);
    }

    @Override
    public DataManagerBuilder<T> serialize(Codec<T> codec) {
        this.codec = codec;
        this.builder.persistent(codec);
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
    public DataManagerBuilder<T> copyOnDeath() {
        this.builder.copyOnDeath();
        return this;
    }

    @Override
    public DataManagerBuilder<T> withDataComponent() {
        this.registerComponentType = true;
        return this;
    }

    @Override
    public DataManager<T> buildAndRegister(String name) {
        ResourceLocation id = new ResourceLocation(modid, name);
        AttachmentType<T> tAttachmentType = this.builder.buildAndRegister(id);
        DataSyncSerializer<T> serializer = null;
        if (syncToClient) {
            if (clientCodec == null) {
                if (codec == null) {
                    throw new IllegalStateException("Cannot sync to client without a stream codec or standard codec");
                } else {
                    clientCodec = ByteBufCodecs.fromCodecWithRegistries(codec);
                }
            }
            serializer = Registry.register(FabricDataLib.SYNC_SERIALIZERS, id, DataSyncSerializer.create(tAttachmentType, clientCodec));
        }
        DataComponentType<T> component = null;
        if (registerComponentType) {
            component = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, DataComponentType.<T>builder().persistent(codec).networkSynchronized(clientCodec).build());
        }
        return new DataManagerImpl<>(tAttachmentType, component, serializer);
    }
}
