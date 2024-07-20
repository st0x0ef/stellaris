package com.st0x0ef.stellaris.common.systems.core.fluid.impl;


import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.common.systems.resources.ResourceStack;
import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;

import java.util.function.Predicate;

public class SimpleFluidSlot implements StorageSlot<FluidResource>, UpdateManager<ResourceStack<FluidResource>> {
    private final long limit;
    private final Runnable update;
    private FluidResource resource;
    private long amount;

    public SimpleFluidSlot(long limit, Runnable update) {
        this.resource = FluidResource.BLANK;
        this.amount = getAmount();
        this.limit = limit;
        this.update = update;
    }

    @Override
    public long getLimit(FluidResource resource) {
        return limit;
    }

    @Override
    public boolean isResourceValid(FluidResource resource) {
        return true;
    }

    @Override
    public FluidResource getResource() {
        return resource;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long insert(FluidResource resource, long amount, boolean simulate) {
        if (!isResourceValid(resource)) return 0;
        if (this.resource.isBlank()) {
            long inserted = Math.min(amount, limit);
            if (!simulate) {
                this.resource = resource;
                this.amount = inserted;
            }
            return inserted;
        } else if (this.resource.equals(resource)) {
            long inserted = Math.min(amount, limit - this.amount);
            if (!simulate) {
                this.amount += inserted;
            }
            return inserted;
        }
        return 0;
    }

    @Override
    public long extract(FluidResource resource, long amount, boolean simulate) {
        if (this.resource.equals(resource)) {
            long extracted = Math.min(amount, this.amount);
            if (!simulate) {
                this.amount -= extracted;
                if (this.amount == 0) {
                    this.resource = FluidResource.BLANK;
                }
            }
            return extracted;
        }
        return 0;
    }

    @Override
    public ResourceStack<FluidResource> createSnapshot() {
        return new ResourceStack<>(resource, amount);
    }

    @Override
    public void readSnapshot(ResourceStack<FluidResource> snapshot) {
        this.resource = snapshot.resource();
        this.amount = snapshot.amount();
    }

    @Override
    public void update() {
        update.run();
    }

    public static class Filtered extends SimpleFluidSlot {
        private final Predicate<FluidResource> filter;

        public Filtered(long limit, Runnable update, Predicate<FluidResource> filter) {
            super(limit, update);
            this.filter = filter;
        }

        @Override
        public boolean isResourceValid(FluidResource resource) {
            return filter.test(resource);
        }
    }
}