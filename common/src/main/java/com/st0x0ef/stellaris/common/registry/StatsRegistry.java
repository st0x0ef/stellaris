package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class StatsRegistry {

    public static final DeferredRegister<ResourceLocation> STATS = DeferredRegister.create(Stellaris.MODID, Registries.CUSTOM_STAT);

    public static StatFormatter KILOMETERS = (value) -> value + " km";

    public static RegistrySupplier<ResourceLocation> SPACE_TRAVEL = STATS.register("space_traveled", () -> ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "space_traveled"));
    public static RegistrySupplier<ResourceLocation> ROCKET_LAUNCHED = STATS.register("rocket_launched", () -> ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "rocket_launched"));

    public static void init() {
        Stats.CUSTOM.get(SPACE_TRAVEL.get(), KILOMETERS);
        Stats.CUSTOM.get(ROCKET_LAUNCHED.get());
    }
}
