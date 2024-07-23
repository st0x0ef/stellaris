package com.st0x0ef.stellaris.platform.systems.core.storage.common;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.platform.systems.resources.Resource;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import java.util.function.Function;

public record CommonWrappedSlotSlot<U extends Resource, V extends TransferVariant<?>> (
        StorageView<V> view, Function<U, V> toVariant, Function<V, U> toresource) implements StorageSlot<U> {

    @Override
    public long getLimit(U resource) {
        return view.getCapacity();
    }

    @Override
    public boolean isResourceValid(U value) {
        return true;
    }

    @Override
    public long insert(U value, long amount, boolean simulate) {
        if (view instanceof SingleSlotStorage<V> slot) {
            try (var transaction = Transaction.openOuter()) {
                long inserted = slot.insert(toVariant.apply(value), amount, transaction);
                if (!simulate) {
                    transaction.commit();
                }
                return inserted;
            }
        } else {
            return 0;
        }
    }

    @Override
    public long extract(U resource, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = view.extract(toVariant.apply(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }

    @Override
    public U getResource() {
        return toresource.apply(view.getResource());
    }

    @Override
    public long getAmount() {
        return view.getAmount();
    }
}
