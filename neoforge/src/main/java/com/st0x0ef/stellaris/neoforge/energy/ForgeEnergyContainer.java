package com.st0x0ef.stellaris.neoforge.energy;

import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.util.Updatable;
import net.neoforged.neoforge.energy.IEnergyStorage;

public record ForgeEnergyContainer<T extends EnergyContainer & Updatable>(T container) implements IEnergyStorage {
    @Override
    public int receiveEnergy(int maxAmount, boolean bl) {
        if (maxAmount <= 0) return 0;
        int inserted = (int) container.insertEnergy(Math.min(maxAmount, container.maxInsert()), bl);
        if (inserted > 0 && !bl) {
            container.update();
        }
        return inserted;
    }

    @Override
    public int extractEnergy(int maxAmount, boolean bl) {
        if (maxAmount <= 0) return 0;
        int extracted = (int) container.extractEnergy(Math.min(maxAmount, container.maxExtract()), bl);
        if (extracted > 0 && !bl) {
            container.update();
        }
        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return (int) container.getStoredEnergy();
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) container.getMaxCapacity();
    }

    @Override
    public boolean canExtract() {
        return container.allowsExtraction();
    }

    @Override
    public boolean canReceive() {
        return container.allowsInsertion();
    }
}
