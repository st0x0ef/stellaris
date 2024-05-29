package com.st0x0ef.stellaris.common.energy.impl;

import net.minecraft.util.Mth;

public class InsertOnlyEnergyContainer extends SimpleEnergyContainer {
    public InsertOnlyEnergyContainer(long energyCapacity) {
        super(energyCapacity);
    }

    public InsertOnlyEnergyContainer(long maxCapacity, long maxInsert) {
        super(maxCapacity, 0L, maxInsert);
    }

    public long maxExtract() {
        return 0L;
    }

    public boolean allowsExtraction() {
        return false;
    }

    public long extractEnergy(long maxAmount, boolean simulate) {
        return 0L;
    }

    public long internalExtract(long maxAmount, boolean simulate) {
        long extracted = Mth.clamp(maxAmount, 0L, this.getStoredEnergy());
        if (simulate) {
            return extracted;
        } else {
            this.setEnergy(this.getStoredEnergy() - extracted);
            return extracted;
        }
    }
}
