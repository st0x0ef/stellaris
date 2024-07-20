package com.st0x0ef.stellaris.common.systems.core.item.impl.vanilla;

import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.core.storage.util.TransferUtil;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import net.minecraft.world.Container;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVanillaContainer implements CommonStorage<ItemResource> {
    Container container;
    List<VanillaDelegatingSlot> slots;

    public AbstractVanillaContainer(Container container) {
        this.container = container;
        this.slots = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            slots.add(new VanillaDelegatingSlot(this, i));
        }
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, resource, amount, simulate);
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, resource, amount, simulate);
    }

    @Override
    public int size() {
        return container.getContainerSize();
    }

    @Override
    public @NotNull StorageSlot<ItemResource> get(int index) {
        return slots.get(index);
    }
}
