package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Stellaris.MODID, Registries.PARTICLE_TYPE);

    /** PARTICLES */
    public static final RegistrySupplier<ParticleType<SimpleParticleType>> VENUS_RAIN_PARTICLE = PARTICLES.register("venus_rain", () -> new SimpleParticleType(true));
    public static final RegistrySupplier<ParticleType<SimpleParticleType>> LARGE_FLAME_PARTICLE = PARTICLES.register("large_flame", () -> new SimpleParticleType(true));
    public static final RegistrySupplier<ParticleType<SimpleParticleType>> LARGE_SMOKE_PARTICLE = PARTICLES.register("large_smoke", () -> new SimpleParticleType(true));
    public static final RegistrySupplier<ParticleType<SimpleParticleType>> SMALL_FLAME_PARTICLE = PARTICLES.register("small_flame", () -> new SimpleParticleType(true));
    public static final RegistrySupplier<ParticleType<SimpleParticleType>> SMALL_SMOKE_PARTICLE = PARTICLES.register("small_smoke", () -> new SimpleParticleType(true));
}
