package com.st0x0ef.stellaris.common.systems.core.fluid.impl;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.fluid.util.FluidStorageData;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.common.systems.core.storage.util.TransferUtil;
import com.st0x0ef.stellaris.common.systems.data.DataManager;
import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SimpleFluidStorage implements CommonStorage<FluidResource>, UpdateManager<FluidStorageData> {
    protected final NonNullList<SimpleFluidSlot> slots;
    private final Runnable update;
    private final long limit;

    public SimpleFluidStorage(int size, long limit) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.limit = limit;
        this.update = () -> {};
    }

    public SimpleFluidStorage(ItemContext context, DataComponentType<FluidStorageData> componentType, int size, long limit) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.limit = limit;
        this.update = () -> {
            context.set(componentType, FluidStorageData.from(this));
            context.updateAll();
        };
        FluidStorageData data = context.getResource().get(componentType);
        if (data != null) readSnapshot(data);
    }

    public SimpleFluidStorage(Object entityOrBlockEntity, DataManager<FluidStorageData> manager, int size, long limit) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.limit = limit;
        this.update = () -> {
            FluidStorageData data = FluidStorageData.from(this);
            manager.set(entityOrBlockEntity, data);
        };
        readSnapshot(manager.get(entityOrBlockEntity));
    }

    public SimpleFluidStorage filter(int slot, Predicate<FluidResource> predicate) {
        slots.set(slot, new SimpleFluidSlot.Filtered(limit, this::update, predicate));
        return this;
    }

    @Override
    public int size() {
        return slots.size();
    }

    @Override
    public @NotNull SimpleFluidSlot get(int index) {
        return slots.get(index);
    }

    @Override
    public FluidStorageData createSnapshot() {
        return FluidStorageData.from(this);
    }

    @Override
    public void readSnapshot(FluidStorageData snapshot) {
        for (int i = 0; i < slots.size() && i < snapshot.stacks().size(); i++) {
            slots.get(i).readSnapshot(snapshot.stacks().get(i));
        }
    }

    @Override
    public void update() {
        update.run();
    }

    @Override
    public long insert(FluidResource resource, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, resource, amount, simulate);
    }

    @Override
    public long extract(FluidResource resource, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, resource, amount, simulate);
    }
}
