package com.st0x0ef.stellaris.fabric.systems.item;

import com.st0x0ef.stellaris.common.systems.item.base.ItemSnapshot;
import com.st0x0ef.stellaris.common.systems.util.Snapshotable;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import com.st0x0ef.stellaris.platform.systems.item.base.ItemContainer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FabricItemContainer extends SnapshotParticipant<ItemSnapshot> implements SlottedStorage<ItemVariant> {
    protected final ItemContainer container;

    public static FabricItemContainer of(ItemContainer container) {
        return container == null ? null : new FabricItemContainer(container);
    }

    public FabricItemContainer(ItemContainer container) {
        this.container = container;
    }

    @Override
    public int getSlotCount() {
        return container.getSlots();
    }

    @Override
    public SingleSlotStorage<ItemVariant> getSlot(int slot) {
        if (slot < 0 || slot >= container.getSlots()) {
            throw new IndexOutOfBoundsException("Slot index out of bounds: " + slot);
        }
        return new SingleItemSlot(slot, this);
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        return container.insertItem(resource.toStack((int) maxAmount), false).getCount();
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        long extracted = 0;
        for (int i = 0; i < container.getSlots(); i++) {
            if (resource.matches(container.getStackInSlot(i))) {
                extracted += container.extractFromSlot(i, (int) maxAmount, false).getCount();
                if (extracted >= maxAmount) {
                    break;
                }
            }
        }
        return extracted;
    }

    @Override
    public Iterator<StorageView<ItemVariant>> iterator() {
        return new Iterator<>() {
            private int slot = 0;

            @Override
            public boolean hasNext() {
                return slot < container.getSlots();
            }

            @Override
            public StorageView<ItemVariant> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Element does not exist at index " + slot);
                }
                return getSlot(slot++);
            }
        };
    }

    @Override
    protected ItemSnapshot createSnapshot() {
        if (container instanceof Snapshotable<?> snapshotable) {
            return (ItemSnapshot) snapshotable.createSnapshot();
        } else {
            throw new NotImplementedException("Container does not implement Snapshotable of ItemSnapshot");
        }
    }

    @Override
    protected void readSnapshot(ItemSnapshot snapshot) {
        snapshot.loadSnapshot();
    }

    @Override
    protected void onFinalCommit() {
        if (container instanceof Updatable updatable) {
            updatable.update();
        }
    }
}
