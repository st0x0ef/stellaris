package com.st0x0ef.stellaris.fabric.systems.energy;

import com.st0x0ef.stellaris.common.systems.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import com.st0x0ef.stellaris.common.systems.energy.base.EnergyContainer;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

public class FabricBlockEnergyContainer<T extends EnergyContainer & Updatable> extends SnapshotParticipant<EnergySnapshot> implements EnergyStorage {
    private final T container;

    public FabricBlockEnergyContainer(T container) {
        this.container = container;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        if(container==null) return 0;
        if (maxAmount <= 0) return 0;
        this.updateSnapshots(transaction);
        return container.insertEnergy(maxAmount, false);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        //if(container==null) return 0;
        if (maxAmount <= 0) return 0;
        this.updateSnapshots(transaction);
        return container.extractEnergy(maxAmount, false);
    }

    @Override
    public long getAmount() {
        //if(container==null) return 0;
        return container.getStoredEnergy();
    }

    @Override
    public long getCapacity() {
        //if(container==null) return 0;
        return container.getMaxCapacity();
    }

    @Override
    public boolean supportsInsertion() {
        //if(container==null) return false;
        return container.allowsInsertion();
    }

    @Override
    public boolean supportsExtraction() {
        //if(container==null) return false;
        return container.allowsExtraction();
    }

    @Override
    protected EnergySnapshot createSnapshot() {
        //if(container==null) return null;
        return container.createSnapshot();
    }

    @Override
    protected void readSnapshot(EnergySnapshot snapshot) {
        //if(container==null) return;
        container.readSnapshot(snapshot);
    }

    @Override
    protected void onFinalCommit() {
        //if(container==null) return;
        container.update();
    }
}
