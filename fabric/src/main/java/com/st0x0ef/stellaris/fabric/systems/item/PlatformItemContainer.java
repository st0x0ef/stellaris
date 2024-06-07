package com.st0x0ef.stellaris.fabric.systems.item;

import com.st0x0ef.stellaris.platform.systems.item.base.ItemContainer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public record PlatformItemContainer(Storage<ItemVariant> storage) implements ItemContainer {

    @Nullable
    public static PlatformItemContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        var itemStorage = ItemStorage.SIDED.find(level, pos, state, entity, direction);
        return itemStorage == null ? null : new PlatformItemContainer(itemStorage);
    }

    @Override
    public int getSlots() {
        AtomicInteger count = new AtomicInteger();
        storage.iterator().forEachRemaining(t -> count.incrementAndGet());
        return count.get();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        Iterator<StorageView<ItemVariant>> it = storage.iterator();
        for (int i = 0; i < slot; i++) {
            it.next();
        }
        return it.next().getResource().toStack();
    }

    @Override
    public int getSlotLimit(int slot) {
        Iterator<StorageView<ItemVariant>> it = storage.iterator();
        for (int i = 0; i < slot; i++) {
            it.next();
        }
        return (int) it.next().getCapacity();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
        try (Transaction tx = Transaction.openOuter()) {
            long inserted = storage.insert(ItemVariant.of(stack), stack.getCount(), tx);
            ItemStack result = stack.copyWithCount((int) inserted);
            if (!simulate) {
                tx.commit();
            }
            return result;
        }
    }

    @Override
    public @NotNull ItemStack insertIntoSlot(int slotIndex, @NotNull ItemStack stack, boolean simulate) {
        if (storage instanceof SlottedStorage<ItemVariant> slottedStorage) {
            try (Transaction tx = Transaction.openOuter()) {
                SingleSlotStorage<ItemVariant> slotStorage = slottedStorage.getSlot(slotIndex);
                long inserted = slotStorage.insert(ItemVariant.of(stack), stack.getCount(), tx);
                ItemStack result = stack.copyWithCount((int) inserted);
                if (!simulate) {
                    tx.commit();
                }
                return result;
            }
        } else {
            return insertItem(stack, simulate);
        }
    }

    @Override
    public @NotNull ItemStack extractItem(int amount, boolean simulate) {
        try (Transaction tx = Transaction.openOuter()) {
            ItemVariant itemVariant = null;
            int amountExtracted = 0;
            for (Iterator<StorageView<ItemVariant>> it = storage.nonEmptyIterator(); it.hasNext(); ) {
                StorageView<ItemVariant> view = it.next();
                if (itemVariant == null) itemVariant = view.getResource();
                if (itemVariant.equals(view.getResource())) {
                    amountExtracted += (int) view.extract(itemVariant, amount - amountExtracted, tx);
                    if (amountExtracted >= amount) {
                        break;
                    }
                }
            }
            if (itemVariant == null || amountExtracted == 0) {
                return ItemStack.EMPTY;
            }
            if (!simulate) {
                tx.commit();
            }
            return itemVariant.toStack(amountExtracted);
        }
    }

    @Override
    public @NotNull ItemStack extractFromSlot(int slot, int amount, boolean simulate) {
        if (storage instanceof SlottedStorage<ItemVariant> slottedStorage) {
            try (Transaction tx = Transaction.openOuter()) {
                SingleSlotStorage<ItemVariant> slotStorage = slottedStorage.getSlot(slot);
                long extracted = slotStorage.extract(slotStorage.getResource(), amount, tx);
                if (extracted > 0) {
                    ItemStack result = slotStorage.getResource().toStack((int) extracted);
                    if (!simulate) {
                        tx.commit();
                    }
                    return result;
                }
            }
        }
        return extractItem(amount, simulate);
    }

    @Override
    public boolean isEmpty() {
        for (Iterator<StorageView<ItemVariant>> it = storage.nonEmptyIterator(); it.hasNext(); ) {
            if (it.next().getAmount() > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void clearContent() {
        for (StorageView<ItemVariant> itemVariantStorageView : storage) {
            itemVariantStorageView.extract(itemVariantStorageView.getResource(), itemVariantStorageView.getAmount(), Transaction.openOuter());
        }
    }
}
