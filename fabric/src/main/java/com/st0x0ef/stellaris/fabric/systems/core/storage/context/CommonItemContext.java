package com.st0x0ef.stellaris.fabric.systems.core.storage.context;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import com.st0x0ef.stellaris.fabric.systems.core.storage.ConversionUtils;
import com.st0x0ef.stellaris.fabric.systems.core.storage.common.CommonWrappedSlotSlot;
import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public record CommonItemContext(ContainerItemContext context) implements ItemContext {
    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        Object2LongLinkedOpenHashMap<ItemVariant> map = new Object2LongLinkedOpenHashMap<>();
        try (var transaction = Transaction.openOuter()) {
            long inserted = context.insert(ConversionUtils.toVariant(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = context.extract(ConversionUtils.toVariant(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }

    @Override
    public long exchange(ItemResource newResource, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long exchanged = context.exchange(ConversionUtils.toVariant(newResource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return exchanged;
        }
    }

    @Override
    public CommonStorage<ItemResource> outerContainer() {
        return new ContextItemContainer(context.getAdditionalSlots(), context::insertOverflow);
    }

    @Override
    public StorageSlot<ItemResource> mainSlot() {
        return new CommonWrappedSlotSlot<>(context.getMainSlot(), ConversionUtils::toVariant, ConversionUtils::toResource);
    }
}
