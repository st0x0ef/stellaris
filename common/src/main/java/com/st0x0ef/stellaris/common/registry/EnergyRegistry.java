package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.energy.EnergyApi;
import com.st0x0ef.stellaris.common.energy.impl.WrappedBlockEnergyContainer;

public class EnergyRegistry {
    public static void Register(){
        EnergyApi.registerEnergyBlock(BlocksRegistry.TEST_BLOCK,);
        EnergyApi.registerEnergyBlockEntity(EntityRegistry.TEST_BLOCK, );

        EnergyApi.registerEnergyBlock(BlocksRegistry.SOLAR_PANEL, (blockPos,blockState)-> new WrappedBlockEnergyContainer());
    }
}
