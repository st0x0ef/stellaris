package com.st0x0ef.stellaris.neoforge.systems.core.item.wrappers;

import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.core.storage.util.TransferUtil;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public record CommonItemContainer(IItemHandler handler) implements CommonStorage<ItemResource> {
    @Override
    public int size() {
        return handler.getSlots();
    }

    @Override
    public @NotNull StorageSlot<ItemResource> get(int index) {
        return new DelegatingItemSlot(handler, index);
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, resource, amount, simulate);
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, resource, amount, simulate);
    }

    public record DelegatingItemSlot(IItemHandler handler, int slot) implements StorageSlot<ItemResource> {

        @Override
        public long getLimit(ItemResource resource) {
            return handler.getSlotLimit(slot);
        }

        @Override
        public boolean isResourceValid(ItemResource resource) {
            return handler.isItemValid(slot, resource.toStack());
        }

        @Override
        public ItemResource getResource() {
            return ItemResource.of(handler.getStackInSlot(slot));
        }

        @Override
        public long getAmount() {
            return handler.getStackInSlot(slot).getCount();
        }

        @Override
        public long insert(ItemResource resource, long amount, boolean simulate) {
            ItemStack leftover = handler.insertItem(slot, resource.toStack((int) amount), simulate);
            return amount - leftover.getCount();
        }

        @Override
        public long extract(ItemResource resource, long amount, boolean simulate) {
            if (!resource.test(handler.getStackInSlot(slot))) {
                return 0;
            }
            ItemStack extracted = handler.extractItem(slot, (int) amount, simulate);
            return extracted.getCount();
        }
    }
}
