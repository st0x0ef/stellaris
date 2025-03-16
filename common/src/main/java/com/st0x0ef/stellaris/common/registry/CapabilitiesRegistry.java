package com.st0x0ef.stellaris.common.registry;

import com.fej1fun.potentials.capabilities.Capabilities;

public class CapabilitiesRegistry {
    public static void init() {
        registerEnergyItems();
        registerFluidItems();
        registerEnergyBlockEntities();
        registerFluidBlockEntities();
    }

    static void registerEnergyBlockEntities() {
        Capabilities.Energy.BLOCK.registerForBlockEntity(BlockEntityRegistry.SOLAR_PANEL);
        Capabilities.Energy.BLOCK.registerForBlockEntity(BlockEntityRegistry.CABLE_ENTITY);
        Capabilities.Energy.BLOCK.registerForBlockEntity(BlockEntityRegistry.COAL_GENERATOR);
        Capabilities.Energy.BLOCK.registerForBlockEntity(BlockEntityRegistry.RADIOACTIVE_GENERATOR);
        Capabilities.Energy.BLOCK.registerForBlockEntity(BlockEntityRegistry.OXYGEN_DISTRIBUTOR);
        Capabilities.Energy.BLOCK.registerForBlockEntity(BlockEntityRegistry.WATER_SEPARATOR_ENTITY);
        Capabilities.Energy.BLOCK.registerForBlockEntity(BlockEntityRegistry.FUEL_REFINERY);
        Capabilities.Energy.BLOCK.registerForBlockEntity(BlockEntityRegistry.PUMPJACK);
        Capabilities.Energy.BLOCK.registerForBlockEntity(BlockEntityRegistry.WATER_PUMP);
        Capabilities.Energy.BLOCK.registerForBlockEntity(BlockEntityRegistry.VACUUMATOR_ENTITY);
    }
    static void registerEnergyItems() {
        Capabilities.Energy.ITEM.registerForItem(ItemsRegistry.OIL_FINDER);
    }

    static void registerFluidBlockEntities() {
        Capabilities.Fluid.BLOCK.registerForBlockEntity(BlockEntityRegistry.WATER_SEPARATOR_ENTITY);
        Capabilities.Fluid.BLOCK.registerForBlockEntity(BlockEntityRegistry.WATER_PUMP);
        Capabilities.Fluid.BLOCK.registerForBlockEntity(BlockEntityRegistry.PUMPJACK);
        Capabilities.Fluid.BLOCK.registerForBlockEntity(BlockEntityRegistry.OXYGEN_DISTRIBUTOR);
        Capabilities.Fluid.BLOCK.registerForBlockEntity(BlockEntityRegistry.PIPE_ENTITY);
    }

    static void registerFluidItems() {
        Capabilities.Fluid.ITEM.registerForItem(ItemsRegistry.OXYGEN_TANK);
        Capabilities.Fluid.ITEM.registerForItem(ItemsRegistry.BIG_OXYGEN_TANK);
        Capabilities.Fluid.ITEM.registerForItem(ItemsRegistry.SPACESUIT_SUIT);
        Capabilities.Fluid.ITEM.registerForItem(ItemsRegistry.JETSUIT_SUIT);
    }
}
