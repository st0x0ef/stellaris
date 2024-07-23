package com.st0x0ef.stellaris.platform.systems.core.energy.impl;

import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.ValueStorage;
import com.st0x0ef.stellaris.platform.systems.data.DataManager;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;

public class SimpleValueStorage implements ValueStorage, UpdateManager<Long> {
    private final long capacity;
    private final Runnable onUpdate;
    private long amount;

    public SimpleValueStorage(long capacity) {
        this.capacity = capacity;
        this.onUpdate = () -> {};
    }

    @SuppressWarnings("DataFlowIssue")
    public SimpleValueStorage(ItemContext context, DataComponentType<Long> componentType, long capacity) {
        this.capacity = capacity;
        this.onUpdate = () -> {
            DataComponentPatch data = DataComponentPatch.builder().set(componentType, this.amount).build();
            context.modify(data);
            context.updateAll();
        };
        if (context.getResource().has(componentType)) {
            this.amount = context.getResource().get(componentType);
        }
    }

    public SimpleValueStorage(Object entityOrBlockEntity, DataManager<Long> dataManager, long capacity) {
        this.capacity = capacity;
        this.onUpdate = () -> dataManager.set(entityOrBlockEntity, this.amount);
        this.amount = dataManager.get(entityOrBlockEntity);
    }

    @Override
    public long getStoredAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    public void set(long amount) {
        this.amount = amount;
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }

    @Override
    public long insert(long amount, boolean simulate) {
        long inserted = Math.min(amount, capacity - this.amount);
        if (!simulate) {
            this.amount += inserted;
        }
        return inserted;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        long extracted = Math.min(amount, this.amount);
        if (!simulate) {
            this.amount -= extracted;
        }
        return extracted;
    }

    @Override
    public Long createSnapshot() {
        return amount;
    }

    @Override
    public void readSnapshot(Long snapshot) {
        this.amount = snapshot;
    }

    @Override
    public void update() {
        onUpdate.run();
    }
}
