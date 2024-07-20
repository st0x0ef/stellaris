package com.st0x0ef.stellaris.common.systems.core.storage.util;

import com.st0x0ef.stellaris.common.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.common.systems.resources.ResourceStack;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import net.minecraft.world.item.ItemStack;

public interface ModifiableItemSlot {
    void setAmount(long amount);

    void setResource(ItemResource resource);

    default void set(ItemStack stack) {
        setResource(ItemResource.of(stack));
        setAmount(stack.getCount());
        UpdateManager.batch(this);
    }

    default void set(ResourceStack<ItemResource> stack) {
        setResource(stack.resource());
        setAmount(stack.amount());
    }

    ItemStack toItemStack();

    int getMaxAllowed(ItemResource resource);

    boolean isEmpty();
}
