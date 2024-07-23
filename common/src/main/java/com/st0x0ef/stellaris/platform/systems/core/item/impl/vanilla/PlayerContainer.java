package com.st0x0ef.stellaris.platform.systems.core.item.impl.vanilla;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.platform.systems.core.storage.util.TransferUtil;
import com.st0x0ef.stellaris.platform.systems.resources.item.ItemResource;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerContainer extends AbstractVanillaContainer implements UpdateManager<PlayerContainer.InventoryState> {
    private final Inventory inventory;
    private final List<ItemStack> toDrop = new ArrayList<>();

    public PlayerContainer(Inventory container) {
        super(container);
        this.inventory = container;
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        return offer(resource, amount, simulate);
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        return 0;
    }

    public long offer(ItemResource resource, long amount, boolean simulate) {
        // try inserting into main hands first, then a subset of the slots between 0 and Inventory.INVENTORY_SIZE, then just the rest of the slots
        long initialAmount = amount;

        // Stack into the main stack first and the offhand stack second.
        for (InteractionHand hand : InteractionHand.values()) {
            StorageSlot<ItemResource> handSlot = getHandSlot(hand);

            if (handSlot.getResource().equals(resource)) {
                amount -= handSlot.insert(resource, amount, simulate);

                if (amount == 0) return initialAmount;
            }
        }

        // Otherwise insert into the main slots, stacking first.
        amount -= TransferUtil.insertSubset(this, 0, Inventory.INVENTORY_SIZE, resource, amount, simulate);

        return initialAmount - amount;
    }

    public long offerOrDrop(ItemResource resource, long amount, boolean simulate) {
        long inserted = offer(resource, amount, simulate);
        if (inserted < amount) {
            drop(resource, amount - inserted, simulate);
        }
        return amount;
    }

    public void drop(ItemResource resource, long amount, boolean simulate) {
        long leftover = amount;
        if (!simulate) {
            ItemStack fakeStack = resource.toStack();
            // add as many fluid stacks as needed to toDrop that are each less than or equal to the max stack amount, but totalling the leftover amount
            while (leftover > 0) {
                int stackSize = (int) Math.min(leftover, fakeStack.getMaxStackSize());
                leftover -= stackSize;
                toDrop.add(resource.toStack(stackSize));
            }
        }
    }

    public StorageSlot<ItemResource> getHandSlot(InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            if (Inventory.isHotbarSlot(inventory.selected)) {
                return get(inventory.selected);
            } else {
                throw new IllegalArgumentException("Main hand is not a hotbar slot: " + inventory.selected);
            }
        } else if (hand == InteractionHand.OFF_HAND) {
            return get(Inventory.SLOT_OFFHAND);
        } else {
            throw new IllegalArgumentException("Invalid hand: " + hand);
        }
    }

    @Override
    public InventoryState createSnapshot() {
        NonNullList<ItemStack> items = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            items.set(i, inventory.getItem(i).copy());
        }
        return new InventoryState(items, toDrop.size());
    }

    @Override
    public void readSnapshot(InventoryState snapshot) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, snapshot.items.get(i));
        }
        if (toDrop.size() > snapshot.droppedItemCount) {
            int previousSize = snapshot.droppedItemCount;

            while (toDrop.size() > previousSize) {
                toDrop.removeLast();
            }
        }
    }

    @Override
    public void update() {
        for (ItemStack stack : toDrop) {
            inventory.player.drop(stack, false);
        }
        toDrop.clear();
    }

    public record InventoryState(NonNullList<ItemStack> items, int droppedItemCount) {}

    public static class AutoDrop extends PlayerContainer {
        public AutoDrop(Inventory container) {
            super(container);
        }

        @Override
        public long insert(ItemResource resource, long amount, boolean simulate) {
            return offerOrDrop(resource, amount, simulate);
        }
    }
}
