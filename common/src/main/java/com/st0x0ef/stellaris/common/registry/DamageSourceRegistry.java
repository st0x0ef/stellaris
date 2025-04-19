package com.st0x0ef.stellaris.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

import static com.st0x0ef.stellaris.Stellaris.id;

public class DamageSourceRegistry {

    public static final ResourceKey<DamageType> RADIATIONS = ResourceKey.create(Registries.DAMAGE_TYPE, id("radiations"));
    public static final ResourceKey<DamageType> OXYGEN = ResourceKey.create(Registries.DAMAGE_TYPE, id("oxygen"));

    public static DamageSource of(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
    }
}
