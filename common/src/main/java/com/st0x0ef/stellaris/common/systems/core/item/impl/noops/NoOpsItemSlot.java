package com.st0x0ef.stellaris.common.systems.core.item.impl.noops;

import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;

public class NoOpsItemSlot implements StorageSlot<ItemResource> {
    public static final NoOpsItemSlot NO_OPS = new NoOpsItemSlot();

    @Override
    public long getLimit(ItemResource resource) {
        return 0;
    }

    @Override
    public boolean isResourceValid(ItemResource resource) {
        return false;
    }

    @Override
    public ItemResource getResource() {
        return ItemResource.BLANK;
    }

    @Override
    public long getAmount() {
        return 0;
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        return 0;
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        return 0;
    }
}
