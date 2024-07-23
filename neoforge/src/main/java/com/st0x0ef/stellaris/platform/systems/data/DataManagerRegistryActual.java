package com.st0x0ef.stellaris.platform.systems.data;

import com.st0x0ef.stellaris.platform.systems.data.impl.DataManagerBuilderImpl;
import com.st0x0ef.stellaris.platform.systems.data.sync.DataSyncSerializer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.msrandom.multiplatform.annotations.Actual;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@Actual
public final class DataManagerRegistryActual {
    private final DeferredRegister<AttachmentType<?>> dataRegistry;
    private final DeferredRegister<DataComponentType<?>> componentRegistry;
    private final DeferredRegister<DataSyncSerializer<?>> serializerRegistry;
    private final String modid;

    @Actual
    public DataManagerRegistryActual(String modid) {
        dataRegistry = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, modid);
        componentRegistry = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, modid);
        serializerRegistry = DeferredRegister.create(NeoDataLib.SYNC_SERIALIZERS, modid);
        this.modid = modid;
    }

    @Actual
    public <T> DataManagerBuilder<T> builder(Supplier<T> factory) {
        return new DataManagerBuilderImpl<>(dataRegistry, componentRegistry, serializerRegistry, factory);
    }

    @Actual
    public void init() {
        ModList.get().getModContainerById(modid).ifPresentOrElse(container -> {
            IEventBus eventBus = container.getEventBus();
            if (eventBus == null) throw new IllegalStateException("Mod " + modid + " has no event bus");
            dataRegistry.register(eventBus);
            componentRegistry.register(eventBus);
            serializerRegistry.register(eventBus);
        }, () -> {
            throw new IllegalStateException("Mod " + modid + " is not loaded");
        });
    }
}
