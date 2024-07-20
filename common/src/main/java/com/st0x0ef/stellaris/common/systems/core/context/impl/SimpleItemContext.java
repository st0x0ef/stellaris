package com.st0x0ef.stellaris.common.systems.core.context.impl;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;

public record SimpleItemContext(CommonStorage<ItemResource> outerContainer, StorageSlot<ItemResource> mainSlot) implements ItemContext {
    public static SimpleItemContext of(CommonStorage<ItemResource> container, int slot) {
        return new SimpleItemContext(container, container.get(slot));
    }
}
