package com.st0x0ef.stellaris.common.systems.core.context;

import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageIO;
import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.common.systems.core.storage.util.TransferUtil;
import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import org.jetbrains.annotations.NotNull;

public interface ItemContext extends StorageIO<ItemResource>, DataComponentHolder {
    default <T> T find(ItemLookup<T, ItemContext> lookup) {
        return lookup.find(getResource().toStack((int) getAmount()), this);
    }

    default boolean isPresent(ItemLookup<?, ItemContext> lookup) {
        return lookup.isPresent(getResource().getCachedStack(), this);
    }

    default ItemResource getResource() {
        return mainSlot().getResource();
    }

    default long getAmount() {
        return mainSlot().getAmount();
    }

    default long insert(ItemResource resource, long amount, boolean simulate) {
        long inserted = mainSlot().insert(resource, amount, simulate);
        long overflow = inserted < amount ? outerContainer().insert(resource, amount - inserted, simulate) : 0;
        if (!simulate) updateAll();
        return inserted + overflow;
    }

    default long extract(ItemResource resource, long amount, boolean simulate) {
        long extract = mainSlot().extract(resource, amount, simulate);
        if (!simulate) updateAll();
        return extract;
    }

    default long exchange(ItemResource newResource, long amount, boolean simulate) {
        long exchange = TransferUtil.exchange(this, getResource(), newResource, amount, simulate);
        if (!simulate) updateAll();
        return exchange;
    }

    /**
     * Applies a data component patch to the item in the main context.
     * @param patch The patch to apply
     * @return Whether the patch was successfully applied
     */
    default boolean modify(DataComponentPatch patch) {
        return exchange(getResource().modify(patch), getAmount(), false) == getAmount();
    }

    default <T> boolean set(DataComponentType<T> type, T value) {
        return modify(DataComponentPatch.builder().set(type, value).build());
    }

    @Override
    default @NotNull DataComponentMap getComponents() {
        return getResource().getComponents();
    }

    CommonStorage<ItemResource> outerContainer();

    StorageSlot<ItemResource> mainSlot();

    default void updateAll() {
        UpdateManager.batch(outerContainer(), mainSlot());
    }
}
