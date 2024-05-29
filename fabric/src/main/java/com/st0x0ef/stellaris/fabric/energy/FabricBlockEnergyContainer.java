package com.st0x0ef.stellaris.fabric.energy;

import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.energy.util.Updatable;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

public class FabricBlockEnergyContainer<T extends EnergyContainer & Updatable> extends SnapshotParticipant<EnergySnapshot> implements EnergyStorage {
    private final T container;

    public FabricBlockEnergyContainer(T container) {
        this.container = container;
    }

    public long insert(long maxAmount, TransactionContext transaction) {
        if (maxAmount <= 0L) {
            return 0L;
        } else {
            this.updateSnapshots(transaction);
            return this.container.insertEnergy(maxAmount, false);
        }
    }

    public long extract(long maxAmount, TransactionContext transaction) {
        if (maxAmount <= 0L) {
            return 0L;
        } else {
            this.updateSnapshots(transaction);
            return this.container.extractEnergy(maxAmount, false);
        }
    }

    public long getAmount() {
        return this.container.getStoredEnergy();
    }

    public long getCapacity() {
        return this.container.getMaxCapacity();
    }

    public boolean supportsInsertion() {
        return this.container.allowsInsertion();
    }

    public boolean supportsExtraction() {
        return this.container.allowsExtraction();
    }

    protected EnergySnapshot createSnapshot() {
        return this.container.createSnapshot();
    }

    protected void readSnapshot(EnergySnapshot snapshot) {
        this.container.readSnapshot(snapshot);
    }

    protected void onFinalCommit() {
        this.container.update();
    }
}
