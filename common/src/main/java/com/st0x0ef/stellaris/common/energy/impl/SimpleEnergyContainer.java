package com.st0x0ef.stellaris.common.energy.impl;

import com.st0x0ef.stellaris.common.energy.EnergyMain;
import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

/**
 * A simple implementation for energy storage.
 * This class should be wrapped by a {@link WrappedBlockEnergyContainer} or a {@link WrappedItemEnergyContainer} to provide the necessary functionality.
 */
public class SimpleEnergyContainer implements EnergyContainer {
    private final long capacity;
    private final long maxInsert;
    private final long maxExtract;
    private long energy;

    public SimpleEnergyContainer(long maxCapacity) {
        this(maxCapacity, 1024L, 1024L);
    }

    public SimpleEnergyContainer(long maxCapacity, long maxTransfer) {
        this(maxCapacity, maxTransfer, maxTransfer);
    }

    public SimpleEnergyContainer(long maxCapacity, long maxExtract, long maxInsert) {
        this.capacity = maxCapacity;
        this.maxExtract = maxExtract;
        this.maxInsert = maxInsert;
    }

    public long insertEnergy(long maxAmount, boolean simulate) {
        long inserted = Mth.clamp(maxAmount, 0L, Math.min(this.maxInsert(), this.getMaxCapacity() - this.getStoredEnergy()));
        if (simulate) {
            return inserted;
        } else {
            this.setEnergy(this.energy + inserted);
            return inserted;
        }
    }

    public long extractEnergy(long maxAmount, boolean simulate) {
        long extracted = Mth.clamp(maxAmount, 0L, Math.min(this.maxExtract(), this.getStoredEnergy()));
        if (simulate) {
            return extracted;
        } else {
            this.setEnergy(this.energy - extracted);
            return extracted;
        }
    }

    public long internalInsert(long maxAmount, boolean simulate) {
        long inserted = Mth.clamp(maxAmount, 0L, this.getMaxCapacity() - this.getStoredEnergy());
        if (simulate) {
            return inserted;
        } else {
            this.setEnergy(this.energy + inserted);
            return inserted;
        }
    }

    public long internalExtract(long maxAmount, boolean simulate) {
        long extracted = Mth.clamp(maxAmount, 0L, this.getStoredEnergy());
        if (simulate) {
            return extracted;
        } else {
            this.setEnergy(this.energy - extracted);
            return extracted;
        }
    }

    public void setEnergy(long energy) {
        this.energy = energy;
    }

    public long getStoredEnergy() {
        return this.energy;
    }

    public long getMaxCapacity() {
        return this.capacity;
    }

    public long maxInsert() {
        return this.maxInsert;
    }

    public long maxExtract() {
        return this.maxExtract;
    }

    public CompoundTag serialize(CompoundTag root) {
        CompoundTag tag = root.getCompound(EnergyMain.STELLARIS_DATA);
        tag.putLong("Energy", this.energy);
        root.put(EnergyMain.STELLARIS_DATA, tag);
        return root;
    }

    public void deserialize(CompoundTag root) {
        CompoundTag tag = root.getCompound(EnergyMain.STELLARIS_DATA);
        this.energy = tag.getLong("Energy");
    }

    public boolean allowsInsertion() {
        return true;
    }

    public boolean allowsExtraction() {
        return true;
    }

    public EnergySnapshot createSnapshot() {
        return new SimpleEnergySnapshot(this);
    }

    public void clearContent() {
        this.energy = 0L;
    }
}
