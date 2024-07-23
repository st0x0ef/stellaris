package com.st0x0ef.stellaris.platform.systems.core.item.impl.noops;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.platform.systems.resources.item.ItemResource;
import org.jetbrains.annotations.NotNull;

public final class NoOpsItemContainer implements CommonStorage<ItemResource> {
    public static final NoOpsItemContainer NO_OPS = new NoOpsItemContainer();

    @Override
    public int size() {
        return 0;
    }

    @Override
    public @NotNull StorageSlot<ItemResource> get(int index) {
        return NoOpsItemSlot.NO_OPS;
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        return 0;
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        return 0;
    }

    @Override
    public boolean allowsInsertion() {
        return false;
    }

    @Override
    public boolean allowsExtraction() {
        return false;
    }
}