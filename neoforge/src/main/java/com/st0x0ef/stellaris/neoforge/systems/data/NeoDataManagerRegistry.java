package com.st0x0ef.stellaris.neoforge.systems.data;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.common.systems.data.DataManager;
import com.st0x0ef.stellaris.common.systems.data.DataManagerBuilder;
import com.st0x0ef.stellaris.common.systems.data.DataManagerRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class NeoDataManagerRegistry implements DataManagerRegistry {
    private final DeferredRegister<AttachmentType<?>> register;
    private final String modid;

    public NeoDataManagerRegistry(String modid) {
        register = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, modid);
        this.modid = modid;
    }

    @Override
    public <T> DataManager<T> register(@NotNull String name, @NotNull Supplier<T> factory, @Nullable Codec<T> codec, boolean copyOnDeath) {
        ResourceLocation id = new ResourceLocation(modid, name);
        var builder = AttachmentType.builder(factory);
        if (codec != null) {
            builder.serialize(codec);
        }

        if (copyOnDeath) {
            builder.copyOnDeath();
        }

        var type = register.register(name, builder::build);
        return new NeoDataManager<>(type);
    }

    @Override
    public <T> DataManagerBuilder<T> builder(Supplier<T> factory) {
        return new NeoDataManagerBuilder<>(register, factory);
    }

    public String getModid() {
        return modid;
    }
}
