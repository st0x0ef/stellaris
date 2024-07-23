package com.st0x0ef.stellaris.platform.systems.core.storage.common;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.ValueStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import team.reborn.energy.api.EnergyStorage;

public record CommonValueStorage(EnergyStorage storage) implements ValueStorage {
    @Override
    public long getStoredAmount() {
        return storage.getAmount();
    }

    @Override
    public long getCapacity() {
        return storage.getCapacity();
    }

    @Override
    public boolean allowsInsertion() {
        return storage.supportsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return storage.supportsExtraction();
    }

    @Override
    public long insert(long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long inserted = storage.insert(amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = storage.extract(amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }
}
