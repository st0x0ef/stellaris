package com.st0x0ef.stellaris.common.energy.impl;


import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;

public class SimpleEnergySnapshot implements EnergySnapshot {
    private final long energy;

    public SimpleEnergySnapshot(EnergyContainer container) {
        this.energy = container.getStoredEnergy();
    }

    public void loadSnapshot(EnergyContainer container) {
        container.setEnergy(this.energy);
    }
}
