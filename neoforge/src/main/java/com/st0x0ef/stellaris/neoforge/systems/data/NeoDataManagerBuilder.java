package com.st0x0ef.stellaris.neoforge.systems.data;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.common.systems.data.DataManager;
import com.st0x0ef.stellaris.common.systems.data.DataManagerBuilder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoDataManagerBuilder<T> implements DataManagerBuilder<T> {
    private final DeferredRegister<AttachmentType<?>> registry;
    private final AttachmentType.Builder<T> builder;

    public NeoDataManagerBuilder(DeferredRegister<AttachmentType<?>> registry, Supplier<T> factory) {
        this.registry = registry;
        this.builder = AttachmentType.builder(factory);
    }

    @Override
    public DataManagerBuilder<T> serialize(Codec<T> codec) {
        builder.serialize(codec);
        return this;
    }

    @Override
    public DataManagerBuilder<T> copyOnDeath() {
        builder.copyOnDeath();
        return this;
    }

    @Override
    public DataManager<T> buildAndRegister(String name) {
        var type = registry.register(name, builder::build);
        return new NeoDataManager<>(type);
    }
}
