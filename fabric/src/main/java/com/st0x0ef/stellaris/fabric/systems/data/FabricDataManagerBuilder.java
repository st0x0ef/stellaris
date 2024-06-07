package com.st0x0ef.stellaris.fabric.systems.data;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.common.systems.data.DataManager;
import com.st0x0ef.stellaris.common.systems.data.DataManagerBuilder;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class FabricDataManagerBuilder<T> implements DataManagerBuilder<T> {
    private final AttachmentRegistry.Builder<T> builder;
    private final String modid;

    public FabricDataManagerBuilder(String modid, Supplier<T> factory) {
        this.builder = AttachmentRegistry.builder();
        this.modid = modid;
        this.builder.initializer(factory);
    }

    @Override
    public DataManagerBuilder<T> serialize(Codec<T> codec) {
        this.builder.persistent(codec);
        return this;
    }

    @Override
    public DataManagerBuilder<T> copyOnDeath() {
        this.builder.copyOnDeath();
        return this;
    }

    @Override
    public DataManager<T> buildAndRegister(String name) {
        AttachmentType<T> tAttachmentType = this.builder.buildAndRegister(new ResourceLocation(modid, name));
        return new FabricDataManager<>(tAttachmentType);
    }
}
