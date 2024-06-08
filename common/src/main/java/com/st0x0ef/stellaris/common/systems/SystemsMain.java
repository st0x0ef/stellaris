package com.st0x0ef.stellaris.common.systems;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.Stellaris;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SystemsMain {
    public static final String MOD_ID = Stellaris.MODID;
    public static final String SYSTEMS_DATA = "StellarisData";
//    public static final Logger LOGGER = (Logger) Stellaris.LOG;

//    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(MOD_ID, Registries.PARTICLE_TYPE);
//
//    public static final Supplier<ParticleType<FluidParticleOptions>> FLUID_PARTICLE = PARTICLES.register("fluid", () -> new ParticleType<>(false, FluidParticleOptions.DESERIALIZER) {
//
//        @Override
//        public MapCodec<FluidParticleOptions> codec() {
//            return FluidParticleOptions.CODEC;
//        }
//
//        @Override
//        public StreamCodec<? super RegistryFriendlyByteBuf, FluidParticleOptions> streamCodec() {
//            return FluidParticleOptions.CODEC_STREAM;
//        }
//    });

//    public static void init() {
//        PARTICLES.register();
//    }

    public static <T, U> Map<T, U> finalizeRegistration(Map<Supplier<T>, U> unfinalized, @Nullable Map<T, U> finalized) {
        if (finalized == null) {
            Map<T, U> collected = unfinalized.entrySet().stream().map(entry -> Pair.of(entry.getKey().get(), entry.getValue())).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond));
            unfinalized.clear();
            return collected;
        }

        return finalized;
    }

}