package com.st0x0ef.stellaris.common.registry;

import com.fej1fun.potentials.components.FluidAmountMapDataComponent;
import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data_components.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.function.UnaryOperator;

public class DataComponentsRegistry {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPE = DeferredRegister.create(Stellaris.MODID, Registries.DATA_COMPONENT_TYPE);

    public static final RegistrySupplier<DataComponentType<RocketComponent>> ROCKET_COMPONENT = register("rocket_component",
            builder -> builder.persistent(RocketComponent.CODEC).networkSynchronized(RocketComponent.STREAM_CODEC));

    public static final RegistrySupplier<DataComponentType<RoverComponent>> ROVER_COMPONENT = register("rover_component",
           builder -> builder.persistent(RoverComponent.CODEC).networkSynchronized(RoverComponent.STREAM_CODEC));

    public static final RegistrySupplier<DataComponentType<JetSuitComponent>> JET_SUIT_COMPONENT = register("jet_suit_component",
            builder -> builder.persistent(JetSuitComponent.CODEC).networkSynchronized(JetSuitComponent.STREAM_CODEC));

    public static final RegistrySupplier<DataComponentType<RadioactiveComponent>> RADIOACTIVE = register("radioactive_component",
            builder -> builder.persistent(RadioactiveComponent.CODEC).networkSynchronized(RadioactiveComponent.STREAM_CODEC));

    public static final RegistrySupplier<DataComponentType<SpaceSuitModules>> SPACE_SUIT_MODULES = register("space_suit_modules",
            builder -> builder.persistent(SpaceSuitModules.CODEC).networkSynchronized(SpaceSuitModules.STREAM_CODEC));

    public static final RegistrySupplier<DataComponentType<FluidAmountMapDataComponent>> FLUID_LIST = register("fluid", builder -> builder
                    .persistent(FluidAmountMapDataComponent.CODEC)
            .networkSynchronized(FluidAmountMapDataComponent.STREAM_CODEC));
    public static final RegistrySupplier<DataComponentType<Integer>> ENERGY =
            register("energy", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    private static <T> RegistrySupplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPE.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

}