package com.st0x0ef.stellaris.fabric.systems.item;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;

public record SingleItemSlot(int slot, FabricItemContainer container) implements SingleSlotStorage<ItemVariant> {

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        container.updateSnapshots(transaction);
        ItemStack inserted = container.container.insertIntoSlot(slot, resource.toStack((int) Math.min(maxAmount, container.container.getSlotLimit(slot))), false);
        return inserted.getCount();
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        if (!resource.matches(container.container.getStackInSlot(slot))) return 0;
        container.updateSnapshots(transaction);
        ItemStack extracted = container.container.extractFromSlot(slot, (int) maxAmount, false);
        return extracted.getCount();
    }

    @Override
    public boolean isResourceBlank() {
        return container.container.getStackInSlot(slot).isEmpty();
    }

    @Override
    public ItemVariant getResource() {
        return ItemVariant.of(container.container.getStackInSlot(slot));
    }

    @Override
    public long getAmount() {
        return container.container.getStackInSlot(slot).getCount();
    }

    @Override
    public long getCapacity() {
        return container.container.getSlotLimit(slot);
    }
}
