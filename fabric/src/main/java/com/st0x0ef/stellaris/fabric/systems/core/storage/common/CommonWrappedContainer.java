package com.st0x0ef.stellaris.fabric.systems.core.storage.common;

import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.resources.Resource;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record CommonWrappedContainer<U extends Resource, V extends TransferVariant<?>>(Storage<V> storage, Function<U, V> toVariant, Function<V, U> toResource) implements CommonStorage<U> {
    public U toResource(V variant) {
        return toResource.apply(variant);
    }

    public V toVariant(U resource) {
        return toVariant.apply(resource);
    }

    @Override
    public int size() {
        if (storage instanceof SlottedStorage<V>) {
            return ((SlottedStorage<V>) storage).getSlotCount();
        }
        int count = 0;
        for (var ignored : storage) {
            count++;
        }
        return count;
    }

    @Override
    public @NotNull StorageSlot<U> get(int index) {
        if (storage instanceof SlottedStorage<V> slotted) {
            return new CommonWrappedSlotSlot<>(slotted.getSlot(index), this::toVariant, this::toResource);
        }
        int i = 0;
        for (var view : storage) {
            if (i == index) {
                return new CommonWrappedSlotSlot<>(view, this::toVariant, this::toResource);
            }
            i++;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public long insert(U resource, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long inserted = storage.insert(toVariant(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(U resource, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = storage.extract(toVariant(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }
}
