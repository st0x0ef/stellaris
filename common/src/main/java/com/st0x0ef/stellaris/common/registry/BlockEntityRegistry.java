package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.GlobeBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.RadioactiveBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.*;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenDistributorBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenPropagatorBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockEntityRegistry {
    //Block entity type
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(Stellaris.MODID, Registries.BLOCK_ENTITY_TYPE);
    public static final RegistrySupplier<BlockEntityType<RocketStationEntity>> ROCKET_STATION = BLOCK_ENTITY_TYPE.register("rocket_station",
            () -> BlockEntityType.Builder.of(RocketStationEntity::new, BlocksRegistry.ROCKET_STATION.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<RadioactiveBlockEntity>> RADIOACTIVE_BLOCK = BLOCK_ENTITY_TYPE.register("radioactive_block",
            () -> BlockEntityType.Builder.of(RadioactiveBlockEntity::new,
                    BlocksRegistry.URANIUM_BLOCK.get(),
                    BlocksRegistry.RAW_URANIUM_BLOCK.get(),
                    BlocksRegistry.MERCURY_URANIUM_ORE.get(),
                    BlocksRegistry.PLUTONIUM_BLOCK.get(),
                    BlocksRegistry.NEPTUNIUM_BLOCK.get()
            ).build(null));
    public static final RegistrySupplier<BlockEntityType<GlobeBlockEntity>> GLOBE_BLOCK_ENTITY = BLOCK_ENTITY_TYPE.register("globe", () -> BlockEntityType.Builder.of(GlobeBlockEntity::new,
                    BlocksRegistry.EARTH_GLOBE_BLOCK.get(),
                    BlocksRegistry.MOON_GLOBE_BLOCK.get(),
                    BlocksRegistry.MARS_GLOBE_BLOCK.get(),
                    BlocksRegistry.MERCURY_GLOBE_BLOCK.get(),
                    BlocksRegistry.VENUS_GLOBE_BLOCK.get())
            .build(null));

    public static final Supplier<BlockEntityType<?>> SOLAR_PANEL = BLOCK_ENTITY_TYPE.register("solar_panel",
            () -> BlockEntityType.Builder.of(SolarPanelEntity::new, BlocksRegistry.SOLAR_PANEL.get()).build(null));
    public static final Supplier<BlockEntityType<?>> COAL_GENERATOR = BLOCK_ENTITY_TYPE.register("coal_generator",
            () -> BlockEntityType.Builder.of(CoalGeneratorEntity::new, BlocksRegistry.COAL_GENERATOR.get()).build(null));
    public static final Supplier<BlockEntityType<?>> RADIOACTIVE_GENERATOR = BLOCK_ENTITY_TYPE.register("radioactive_generator",
            () -> BlockEntityType.Builder.of(RadioactiveGeneratorEntity::new, BlocksRegistry.RADIOACTIVE_GENERATOR.get()).build(null));
    public static final Supplier<BlockEntityType<?>> CABLE_ENTITY = BLOCK_ENTITY_TYPE.register("cable",
            () -> BlockEntityType.Builder.of(CableBlockEntity::new, BlocksRegistry.CABLE.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<VacumatorBlockEntity>> VACUMATOR_ENTITY = BLOCK_ENTITY_TYPE.register("vacumator",
            () -> BlockEntityType.Builder.of(VacumatorBlockEntity::new, BlocksRegistry.VACUMATOR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<WaterSeparatorBlockEntity>> WATER_SEPARATOR_ENTITY = BLOCK_ENTITY_TYPE.register("water_separator",
            () -> BlockEntityType.Builder.of(WaterSeparatorBlockEntity::new, BlocksRegistry.WATER_SEPARATOR.get()).build(null));
    public static final Supplier<BlockEntityType<?>> OXYGEN_PROPAGATOR = BLOCK_ENTITY_TYPE.register("oxygen_propagator",
            () -> BlockEntityType.Builder.of(OxygenPropagatorBlockEntity::new, BlocksRegistry.OXYGEN_PROPAGATOR.get()).build(null));
    public static final Supplier<BlockEntityType<?>> OXYGEN_DISTRIBUTOR = BLOCK_ENTITY_TYPE.register("oxygen_distributor",
            () -> BlockEntityType.Builder.of(OxygenDistributorBlockEntity::new, BlocksRegistry.OXYGEN_DISTRIBUTOR.get()).build(null));

}
