package com.st0x0ef.stellaris.common.systems.energy.impl;

import com.st0x0ef.stellaris.platform.systems.energy.EnergyContainer;
import com.st0x0ef.stellaris.common.systems.energy.base.EnergySnapshot;

public class SimpleEnergySnapshot implements EnergySnapshot {
    private final long energy;

    public SimpleEnergySnapshot(EnergyContainer container) {
        this.energy = container.getStoredEnergy();
    }

    @Override
    public void loadSnapshot(EnergyContainer container) {
        container.setEnergy(energy);
    }
}