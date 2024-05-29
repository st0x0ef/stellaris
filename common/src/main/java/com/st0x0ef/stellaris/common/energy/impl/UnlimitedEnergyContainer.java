package com.st0x0ef.stellaris.common.energy.impl;


import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.energy.util.Updatable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class UnlimitedEnergyContainer implements EnergyContainer, Updatable {
    public UnlimitedEnergyContainer() {
    }

    public long insertEnergy(long maxAmount, boolean simulate) {
        return 0L;
    }

    public long extractEnergy(long maxAmount, boolean simulate) {
        return 2147483647L;
    }

    public void setEnergy(long energy) {
    }

    public long getStoredEnergy() {
        return 2147483647L;
    }

    public long getMaxCapacity() {
        return 2147483647L;
    }

    public long maxInsert() {
        return 0L;
    }

    public long maxExtract() {
        return 2147483647L;
    }

    public boolean allowsInsertion() {
        return false;
    }

    public boolean allowsExtraction() {
        return true;
    }

    public EnergySnapshot createSnapshot() {
        return new SimpleEnergySnapshot(this);
    }

    public void deserialize(CompoundTag nbt, HolderLookup.Provider provider) {
    }

    public CompoundTag serialize(CompoundTag nbt, HolderLookup.Provider provider) {
        return nbt;
    }

    public void update() {
    }

    public void clearContent() {
    }
}
