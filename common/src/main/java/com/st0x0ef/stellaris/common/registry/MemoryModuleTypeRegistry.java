package com.st0x0ef.stellaris.common.registry;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;

public class MemoryModuleTypeRegistry {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES =
            DeferredRegister.create(Stellaris.MODID, Registries.MEMORY_MODULE_TYPE);

    public static final RegistrySupplier<MemoryModuleType<Integer>> SWITCH_COOLDOWN =
            MEMORY_MODULE_TYPES.register("switch_cooldown",
            () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
    public static final RegistrySupplier<MemoryModuleType<Integer>> SPIKES_COOLDOWN =
            MEMORY_MODULE_TYPES.register("spikes_cooldown",
                    () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
    public static final RegistrySupplier<MemoryModuleType<Integer>> CHEESE_RAIN_COOLDOWN =
            MEMORY_MODULE_TYPES.register("cheese_rain_cooldown",
                    () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
    public static final RegistrySupplier<MemoryModuleType<Integer>> CHEESE_WHEEL_COOLDOWN =
            MEMORY_MODULE_TYPES.register("cheese_wheel_cooldown",
                    () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
    public static final RegistrySupplier<MemoryModuleType<Integer>> MOLD_BOMB_COOLDOWN =
            MEMORY_MODULE_TYPES.register("mold_bomb_cooldown",
                    () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
    public static final RegistrySupplier<MemoryModuleType<String>> CURRENT_PHASE =
            MEMORY_MODULE_TYPES.register("current_phase",
                    () -> new MemoryModuleType<>(Optional.of(Codec.STRING)));
}
