package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.blocks.entities.machines.*;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenDistributorBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenPropagatorBlockEntity;
import com.st0x0ef.stellaris.common.systems.energy.EnergyApi;

public class LookupApiRegistry {
    public static void registerEnergy() {

        EnergyApi.registerEnergyBlock(BlocksRegistry.CABLE, (level, blockPos, blockState, blockEntity, direction) -> {
            if(blockEntity instanceof CableBlockEntity blockEntity1) blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.CABLE_ENTITY, (level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof CableBlockEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);;
            return null;
        });

        EnergyApi.registerEnergyBlock(BlocksRegistry.OXYGEN_PROPAGATOR, (level, blockPos, blockState, blockEntity, direction) -> {
            if(blockEntity instanceof OxygenPropagatorBlockEntity blockEntity1) blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.OXYGEN_PROPAGATOR, (level, blockPos, blockState, blockEntity, direction) -> {
            if(blockEntity instanceof OxygenPropagatorBlockEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });

        EnergyApi.registerEnergyBlock(BlocksRegistry.OXYGEN_DISTRIBUTOR, (level, blockPos, blockState, blockEntity, direction) -> {
            if(blockEntity instanceof OxygenDistributorBlockEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.OXYGEN_DISTRIBUTOR, (level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof OxygenDistributorBlockEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });

        EnergyApi.registerEnergyBlock(BlocksRegistry.WATER_SEPARATOR, (level, blockPos, blockState, blockEntity, direction) -> {
            if(blockEntity instanceof WaterSeparatorBlockEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.WATER_SEPARATOR_ENTITY, (level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof WaterSeparatorBlockEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });

        EnergyApi.registerEnergyBlock(BlocksRegistry.FUEL_REFINERY, (level, blockPos, blockState, blockEntity, direction) -> {
            if(blockEntity instanceof FuelRefineryBlockEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.FUEL_REFINERY, (level, blockPos, blockState, blockEntity, direction) -> {
            if(blockEntity instanceof FuelRefineryBlockEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });


        EnergyApi.registerEnergyBlock(BlocksRegistry.SOLAR_PANEL, (level, blockPos, blockState, blockEntity, direction)-> {
            if(blockEntity instanceof SolarPanelEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.SOLAR_PANEL, (level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof SolarPanelEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });

        EnergyApi.registerEnergyBlock(BlocksRegistry.COAL_GENERATOR, (level, blockPos, blockState, blockEntity, direction)-> {
            if(blockEntity instanceof CoalGeneratorEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.COAL_GENERATOR, (level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof CoalGeneratorEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });

        EnergyApi.registerEnergyBlock(BlocksRegistry.RADIOACTIVE_GENERATOR, (level, blockPos, blockState, blockEntity, direction)-> {
            if(blockEntity instanceof RadioactiveGeneratorEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.RADIOACTIVE_GENERATOR, (level, blockPos, blockState, blockEntity, direction) ->{
            if(blockEntity instanceof RadioactiveGeneratorEntity blockEntity1) return blockEntity1.getEnergyStorage(level, blockPos, blockState, blockEntity, direction);
            return null;
        });

    }

}
