package com.st0x0ef.stellaris.platform.systems.core.storage.fabric;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.platform.systems.resources.Resource;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public record FabricWrappedSlot<U extends Resource, V extends TransferVariant<?>>(StorageSlot<U> container,
                                                                                  @Nullable OptionalSnapshotParticipant<?> updateManager,
                                                                                  Function<V, U> toResource,
                                                                                  Function<U, V> toVariant) implements SingleSlotStorage<V> {

    public FabricWrappedSlot(StorageSlot<U> container, Function<U, V> toVariant, Function<V, U> toresource) {
        this(container, OptionalSnapshotParticipant.of(container), toresource, toVariant);
    }

    @Override
    public long insert(V resource, long maxAmount, TransactionContext transaction) {
        U holder = toResource.apply(resource);
        updateSnapshots(transaction);
        return container.insert(holder, maxAmount, false);
    }

    @Override
    public long extract(V resource, long maxAmount, TransactionContext transaction) {
        U holder = toResource.apply(resource);
        updateSnapshots(transaction);
        return container.extract(holder, maxAmount, false);
    }

    @Override
    public boolean isResourceBlank() {
        return container.getResource().isBlank();
    }

    @Override
    public V getResource() {
        return toVariant.apply(container.getResource());
    }

    @Override
    public long getAmount() {
        return container.getAmount();
    }

    @Override
    public long getCapacity() {
        return container.getLimit(container.getResource());
    }

    private void updateSnapshots(TransactionContext transaction) {
        if (updateManager != null) {
            updateManager.updateSnapshots(transaction);
        }
    }
}
