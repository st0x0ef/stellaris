package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data_components.JetSuitComponent;
import com.st0x0ef.stellaris.common.data_components.RocketComponent;
import com.st0x0ef.stellaris.common.data_components.SpaceArmorComponent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class DataComponentsRegistry {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPE = DeferredRegister.create(Stellaris.MODID, Registries.DATA_COMPONENT_TYPE);

    public static final RegistrySupplier<DataComponentType<RocketComponent>> ROCKET_COMPONENT = DATA_COMPONENT_TYPE.register("rocket_component",
            () -> DataComponentType.<RocketComponent>builder().persistent(RocketComponent.CODEC).networkSynchronized(RocketComponent.STREAM_CODEC).build());

    public static final RegistrySupplier<DataComponentType<SpaceArmorComponent>> SPACE_ARMOR_ROCKET = DATA_COMPONENT_TYPE.register("space_armor_component",
            () -> DataComponentType.<SpaceArmorComponent>builder().persistent(SpaceArmorComponent.CODEC).networkSynchronized(SpaceArmorComponent.STREAM_CODEC).build());

    public static final RegistrySupplier<DataComponentType<JetSuitComponent>> JET_SUIT_COMPONENT = DATA_COMPONENT_TYPE.register("jet_suit_component",
            () -> DataComponentType.<JetSuitComponent>builder().persistent(JetSuitComponent.CODEC).networkSynchronized(JetSuitComponent.STREAM_CODEC).build());

}
