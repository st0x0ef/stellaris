package com.st0x0ef.stellaris.neoforge.systems.core.energy.wrappers;

import com.st0x0ef.stellaris.common.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.common.systems.core.storage.base.ValueStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public record NeoEnergyContainer(ValueStorage container) implements IEnergyStorage {
    @Override
    public int receiveEnergy(int i, boolean bl) {
        long inserted = container.insert(i, bl);
        if (!bl) UpdateManager.batch(container);
        return (int) inserted;
    }

    @Override
    public int extractEnergy(int i, boolean bl) {
        long extracted = container.extract(i, bl);
        if (!bl) UpdateManager.batch(container);
        return (int) extracted;
    }

    @Override
    public int getEnergyStored() {
        return (int) container.getStoredAmount();
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) container.getCapacity();
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
