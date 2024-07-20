package com.st0x0ef.stellaris.fabric.systems.core.storage.fabric;

import com.st0x0ef.stellaris.common.systems.core.storage.base.ValueStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

public record FabricLongStorage(ValueStorage container,
                                OptionalSnapshotParticipant<?> snapshotParticipant) implements EnergyStorage {

    public FabricLongStorage(ValueStorage container) {
        this(container, OptionalSnapshotParticipant.of(container));
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        return container.insert(maxAmount, false);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        return container.extract(maxAmount, false);
    }

    @Override
    public long getAmount() {
        return container.getStoredAmount();
    }

    @Override
    public long getCapacity() {
        return container.getCapacity();
    }

    private void updateSnapshots(TransactionContext transaction) {
        if (snapshotParticipant != null) {
            snapshotParticipant.updateSnapshots(transaction);
        }
    }
}
