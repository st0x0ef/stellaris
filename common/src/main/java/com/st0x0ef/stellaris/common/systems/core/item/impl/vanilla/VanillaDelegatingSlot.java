package com.st0x0ef.stellaris.common.systems.core.item.impl.vanilla;

import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class VanillaDelegatingSlot implements StorageSlot<ItemResource>, UpdateManager<ItemStack> {
    private final int slot;
    private final Container container;
    private final Runnable update;

    public VanillaDelegatingSlot(AbstractVanillaContainer container, int slot) {
        this.container = container.container;
        this.update = container instanceof UpdateManager<?> manager ? manager::update : () -> {};
        this.slot = slot;
    }

    @Override
    public long getLimit(ItemResource resource) {
        return container.getMaxStackSize(resource.getCachedStack());
    }

    @Override
    public boolean isResourceValid(ItemResource resource) {
        return container.canPlaceItem(slot, resource.toStack());
    }

    @Override
    public ItemResource getResource() {
        return ItemResource.of(container.getItem(slot));
    }

    @Override
    public long getAmount() {
        return container.getItem(slot).getCount();
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        ItemStack stack = container.getItem(slot);
        if (resource.test(stack) || stack.isEmpty()) {
            if (stack.isEmpty()) {
                ItemStack inserted = resource.toStack(Math.min((int) amount, (int) getLimit(resource)));
                if (!simulate) {
                    container.setItem(slot, inserted);
                }
                return inserted.getCount();
            } else {
                ItemStack inserted = resource.toStack(Math.min((int) amount + stack.getCount(), (int) getLimit(resource)));
                if (!simulate) {
                    container.setItem(slot, inserted);
                }
                return inserted.getCount() - stack.getCount();
            }
        }
        return 0;
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        ItemStack stack = container.getItem(slot).copy();
        if (resource.test(stack)) {
            ItemStack extracted = stack.split((int) amount);
            if (!simulate) {
                if (stack.isEmpty()) {
                    container.setItem(slot, ItemStack.EMPTY);
                } else {
                    container.setItem(slot, stack);
                }
            }
            return extracted.getCount();
        }
        return 0;
    }

    @Override
    public ItemStack createSnapshot() {
        return container.getItem(slot).copy();
    }

    @Override
    public void readSnapshot(ItemStack snapshot) {
        container.setItem(slot, snapshot);
    }

    @Override
    public void update() {
        update.run();
    }
}