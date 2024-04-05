package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.common.energy.EnergyApi;

public class EnergyRegistry {
    public static void Register(){
        EnergyApi.registerEnergyBlock(BlocksRegistry.TEST_BLOCK,EnergyApi.getEnergyBlock(BlocksRegistry.TEST_BLOCK.get()));
    }
}
