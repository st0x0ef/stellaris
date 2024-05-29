package com.st0x0ef.stellaris.common.energy.impl;

import net.minecraft.util.Mth;

public class ExtractOnlyEnergyContainer extends SimpleEnergyContainer {
    public ExtractOnlyEnergyContainer(long energyCapacity) {
        super(energyCapacity);
    }

    public ExtractOnlyEnergyContainer(long maxCapacity, long maxExtract) {
        super(maxCapacity, maxExtract, 0L);
    }

    public long maxInsert() {
        return 0L;
    }

    public boolean allowsInsertion() {
        return false;
    }

    public long insertEnergy(long maxAmount, boolean simulate) {
        return 0L;
    }

    public long internalInsert(long maxAmount, boolean simulate) {
        long inserted = Mth.clamp(maxAmount, 0L, this.getMaxCapacity() - this.getStoredEnergy());
        if (simulate) {
            return inserted;
        } else {
            this.setEnergy(this.getStoredEnergy() + inserted);
            return inserted;
        }
    }
}