package com.st0x0ef.stellaris.platform.systems.core.item.wrappers;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.platform.systems.resources.item.ItemResource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public record NeoItemHandler(CommonStorage<ItemResource> container) implements IItemHandler {
    @Override
    public int getSlots() {
        return container.size();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        StorageSlot<ItemResource> slot = container.get(i);
        return slot.getResource().toStack((int) slot.getAmount());
    }

    @Override
    public ItemStack insertItem(int i, ItemStack arg, boolean bl) {
        long inserted = container.get(i).insert(ItemResource.of(arg), arg.getCount(), bl);
        if (!bl) {
            UpdateManager.batch(container);
        }
        return arg.getCount() == inserted ? ItemStack.EMPTY : arg.copyWithCount((int) (arg.getCount() - inserted));
    }

    @Override
    public ItemStack extractItem(int i, int j, boolean bl) {
        ItemResource resource = ItemResource.of(getStackInSlot(i));
        long extracted = container.get(i).extract(resource, j, bl);
        if (!bl) {
            UpdateManager.batch(container);
        }
        return resource.toStack((int) extracted);
    }

    @Override
    public int getSlotLimit(int i) {
        StorageSlot<ItemResource> slot = container.get(i);
        return (int) slot.getLimit(slot.getResource());
    }

    @Override
    public boolean isItemValid(int i, ItemStack arg) {
        return container.get(i).isResourceValid(ItemResource.of(arg));
    }
}
