package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.blocks.entities.machines.*;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenDistributorBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.oxygen.OxygenPropagatorBlockEntity;
import com.st0x0ef.stellaris.common.systems.energy.EnergyApi;
import com.st0x0ef.stellaris.common.systems.energy.impl.ExtractOnlyEnergyContainer;
import com.st0x0ef.stellaris.common.systems.energy.impl.SimpleEnergyContainer;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;

public class LookupApiRegistry {
    public static void register() {

        EnergyApi.registerEnergyBlock(BlocksRegistry.CABLE, (level, blockPos, blockState, blockEntity, direction)
                -> new WrappedBlockEnergyContainer(blockEntity, new SimpleEnergyContainer(1000, Integer.MAX_VALUE)));
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.CABLE_ENTITY,(level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof CableBlockEntity blockEntity1){
                return blockEntity1.getWrappedEnergyContainer();
            } return null;
        });
        EnergyApi.registerEnergyBlock(BlocksRegistry.OXYGEN_PROPAGATOR, (level, blockPos, blockState, blockEntity, direction)
                -> new WrappedBlockEnergyContainer(blockEntity, new SimpleEnergyContainer(6000, Integer.MAX_VALUE)));
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.OXYGEN_PROPAGATOR,(level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof OxygenPropagatorBlockEntity blockEntity1){
                return blockEntity1.getWrappedEnergyContainer();
            } return null;
        });
        EnergyApi.registerEnergyBlock(BlocksRegistry.OXYGEN_DISTRIBUTOR, (level, blockPos, blockState, blockEntity, direction)
                -> new WrappedBlockEnergyContainer(blockEntity, new SimpleEnergyContainer(6000, Integer.MAX_VALUE)));
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.OXYGEN_DISTRIBUTOR,(level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof OxygenDistributorBlockEntity blockEntity1){
                return blockEntity1.getWrappedEnergyContainer();
            } return null;
        });
        EnergyApi.registerEnergyBlock(BlocksRegistry.WATER_SEPARATOR, (level, blockPos, blockState, blockEntity, direction)
                -> new WrappedBlockEnergyContainer(blockEntity, new SimpleEnergyContainer(12000, Integer.MAX_VALUE)));
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.WATER_SEPARATOR_ENTITY,(level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof WaterSeparatorBlockEntity blockEntity1){
                return blockEntity1.getWrappedEnergyContainer();
            } return null;
        });
        EnergyApi.registerEnergyBlock(BlocksRegistry.WATER_SEPARATOR, (level, blockPos, blockState, blockEntity, direction)
                -> new WrappedBlockEnergyContainer(blockEntity, new SimpleEnergyContainer(128000, Integer.MAX_VALUE)));
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.OXYGEN_DISTRIBUTOR,(level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof WaterSeparatorBlockEntity blockEntity1){
                return blockEntity1.getWrappedEnergyContainer();
            } return null;
        });

        EnergyApi.registerEnergyBlock(BlocksRegistry.SOLAR_PANEL, (level, blockPos, blockState, blockEntity, direction)
                -> new WrappedBlockEnergyContainer(blockEntity, new ExtractOnlyEnergyContainer(30000, Integer.MAX_VALUE)));
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.SOLAR_PANEL,(level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof SolarPanelEntity blockEntity1){
                return blockEntity1.getWrappedEnergyContainer();
            } return null;
        });
        EnergyApi.registerEnergyBlock(BlocksRegistry.COAL_GENERATOR, (level, blockPos, blockState, blockEntity, direction)
                -> new WrappedBlockEnergyContainer(blockEntity, new ExtractOnlyEnergyContainer(30000, Integer.MAX_VALUE)));
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.COAL_GENERATOR,(level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof CoalGeneratorEntity blockEntity1){
                return blockEntity1.getWrappedEnergyContainer();
            } return null;
        });
        EnergyApi.registerEnergyBlock(BlocksRegistry.RADIOACTIVE_GENERATOR, (level, blockPos, blockState, blockEntity, direction)
                -> new WrappedBlockEnergyContainer(blockEntity, new ExtractOnlyEnergyContainer(1000000, Integer.MAX_VALUE)));
        EnergyApi.registerEnergyBlockEntity(BlockEntityRegistry.RADIOACTIVE_GENERATOR,(level, blockPos, blockState, blockEntity, direction)->{
            if(blockEntity instanceof RadioactiveGeneratorEntity blockEntity1){
                return blockEntity1.getWrappedEnergyContainer();
            } return null;
        });

    }
}
