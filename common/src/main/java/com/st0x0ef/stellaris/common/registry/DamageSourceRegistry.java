package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class DamageSourceRegistry {

    public static final ResourceKey<DamageType> RADIATIONS = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocationUtils.id("radiations"));
    public static final ResourceKey<DamageType> OXYGEN = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocationUtils.id("oxygen"));

    public static DamageSource of(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }
}
