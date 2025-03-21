package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import org.jetbrains.annotations.Nullable;

public class StatsRegistry {

    public static final DeferredRegister<ResourceLocation> STATS = DeferredRegister.create(Stellaris.MODID, Registries.CUSTOM_STAT);

    public static RegistrySupplier<ResourceLocation> SPACE_TRAVEL = register("space_traveled", (value) -> value + " km");
    public static RegistrySupplier<ResourceLocation> ROCKET_LAUNCHED = register("rocket_launched");

    public static RegistrySupplier<ResourceLocation> register(String key) {
        return register(key, StatFormatter.DEFAULT);
    }

    public static RegistrySupplier<ResourceLocation> register(String key, StatFormatter formatter) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, key);
        RegistrySupplier<ResourceLocation> supplier = STATS.register(key, () -> resourceLocation);
        //Stats.CUSTOM.get(resourceLocation, formatter); // TODO : find a way to make this working on neoforge
        return supplier;
    }
}
