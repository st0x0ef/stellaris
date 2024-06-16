package com.st0x0ef.stellaris.neoforge.systems.energy;

import com.st0x0ef.stellaris.common.systems.util.Updatable;
import com.st0x0ef.stellaris.platform.systems.energy.EnergyContainer;
import net.neoforged.neoforge.energy.IEnergyStorage;

public record ForgeEnergyContainer<T extends EnergyContainer & Updatable>(T container) implements IEnergyStorage {
    @Override
    public int receiveEnergy(int maxAmount, boolean bl) {
        if(container==null) return 0;
        if (maxAmount <= 0) return 0;
        int inserted = (int) container.insertEnergy(Math.min(maxAmount, container.maxInsert()), bl);
        if (inserted > 0 && !bl) {
            container.update();
        }
        return inserted;
    }

    @Override
    public int extractEnergy(int maxAmount, boolean bl) {
        //if(container==null) return 0;
        if (maxAmount <= 0) return 0;
        int extracted = (int) container.extractEnergy(Math.min(maxAmount, container.maxExtract()), bl);
        if (extracted > 0 && !bl) {
            container.update();
        }
        return extracted;
    }

    @Override
    public int getEnergyStored() {
        //if(container==null) return 0;
        return (int) container.getStoredEnergy();
    }

    @Override
    public int getMaxEnergyStored() {
        //if(container==null) return 0;
        return (int) container.getMaxCapacity();
    }

    @Override
    public boolean canExtract() {
        //if(container==null) return false;
        return container.allowsExtraction();
    }

    @Override
    public boolean canReceive() {
        //if(container==null) return false;
        return container.allowsInsertion();
    }
}
