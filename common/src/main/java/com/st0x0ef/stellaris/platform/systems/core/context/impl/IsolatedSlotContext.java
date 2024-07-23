package com.st0x0ef.stellaris.platform.systems.core.context.impl;

import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.item.impl.SimpleItemSlot;
import com.st0x0ef.stellaris.platform.systems.core.item.impl.noops.NoOpsItemContainer;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.resources.item.ItemResource;
import net.minecraft.world.item.ItemStack;

public record IsolatedSlotContext(SimpleItemSlot mainSlot) implements ItemContext {
    public IsolatedSlotContext(ItemStack stack) {
        this(new SimpleItemSlot(stack));
    }

    @Override
    public CommonStorage<ItemResource> outerContainer() {
        return NoOpsItemContainer.NO_OPS;
    }
}
