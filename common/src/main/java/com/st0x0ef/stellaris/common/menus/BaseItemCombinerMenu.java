package com.st0x0ef.stellaris.common.menus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class BaseItemCombinerMenu extends AbstractContainerMenu {
    protected final ContainerLevelAccess access;
    protected final Player player;
    protected final Container inputSlots;
    private final List<Integer> inputSlotIndexes;
    protected final ResultContainer resultSlots = new ResultContainer();
    private final int resultSlotIndex;

    protected abstract boolean mayPickup(Player player, boolean hasStack);

    protected abstract void onTake(Player player, ItemStack stack);

    protected abstract boolean isValidBlock(BlockState state);

    public BaseItemCombinerMenu(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId);
        this.access = access;
        this.player = playerInventory.player;
        ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition = this.createInputSlotDefinitions();
        this.inputSlots = this.createContainer(itemCombinerMenuSlotDefinition.getNumOfInputSlots());
        this.inputSlotIndexes = itemCombinerMenuSlotDefinition.getInputSlotIndexes();
        this.resultSlotIndex = itemCombinerMenuSlotDefinition.getResultSlotIndex();
        this.createInputSlots(itemCombinerMenuSlotDefinition);
        this.createResultSlot(itemCombinerMenuSlotDefinition);
        this.createInventorySlots(playerInventory);
    }

    private void createInputSlots(ItemCombinerMenuSlotDefinition slotDefinition) {
        for(final ItemCombinerMenuSlotDefinition.SlotDefinition slotDefinition2 : slotDefinition.getSlots()) {
            this.addSlot(new Slot(this.inputSlots, slotDefinition2.slotIndex(), slotDefinition2.x(), slotDefinition2.y()) {
                public boolean mayPlace(ItemStack stack) {
                    return slotDefinition2.mayPlace().test(stack);
                }
            });
        }

    }

    private void createResultSlot(ItemCombinerMenuSlotDefinition slotDefinition) {
        this.addSlot(new Slot(this.resultSlots, slotDefinition.getResultSlot().slotIndex(), slotDefinition.getResultSlot().x(), slotDefinition.getResultSlot().y()) {
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public boolean mayPickup(Player player) {
                return BaseItemCombinerMenu.this.mayPickup(player, this.hasItem());
            }

            public void onTake(Player player, ItemStack stack) {
                BaseItemCombinerMenu.this.onTake(player, stack);
            }
        });
    }

    private void createInventorySlots(Inventory inventory) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 10 + j * 18, 106 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 10 + i * 18, 164));
        }

    }

    public abstract void createResult();

    protected abstract ItemCombinerMenuSlotDefinition createInputSlotDefinitions();

    private SimpleContainer createContainer(int size) {
        return new SimpleContainer(size) {
            public void setChanged() {
                super.setChanged();
                BaseItemCombinerMenu.this.slotsChanged(this);
            }
        };
    }

    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (container == this.inputSlots) {
            this.createResult();
        }

    }

    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.inputSlots));
    }

    public boolean stillValid(Player player) {
        return this.access.evaluate((level, blockPos) -> this.isValidBlock(level.getBlockState(blockPos)) && player.canInteractWithBlock(blockPos, 4.0F), true);
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            int i = this.getInventorySlotStart();
            int j = this.getUseRowEnd();
            if (index == this.getResultSlot()) {
                if (!this.moveItemStackTo(itemStack2, i, j, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);
            } else if (this.inputSlotIndexes.contains(index)) {
                if (!this.moveItemStackTo(itemStack2, i, j, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.canMoveIntoInputSlots(itemStack2) && index >= this.getInventorySlotStart() && index < this.getUseRowEnd()) {
                int k = this.getSlotToQuickMoveTo(itemStack);
                if (!this.moveItemStackTo(itemStack2, k, this.getResultSlot(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= this.getInventorySlotStart() && index < this.getInventorySlotEnd()) {
                if (!this.moveItemStackTo(itemStack2, this.getUseRowStart(), this.getUseRowEnd(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= this.getUseRowStart() && index < this.getUseRowEnd() && !this.moveItemStackTo(itemStack2, this.getInventorySlotStart(), this.getInventorySlotEnd(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
        }

        return itemStack;
    }

    protected boolean canMoveIntoInputSlots(ItemStack stack) {
        return true;
    }

    public int getSlotToQuickMoveTo(ItemStack stack) {
        return this.inputSlots.isEmpty() ? 0 : this.inputSlotIndexes.getFirst();
    }

    public int getResultSlot() {
        return this.resultSlotIndex;
    }

    private int getInventorySlotStart() {
        return this.getResultSlot() + 1;
    }

    private int getInventorySlotEnd() {
        return this.getInventorySlotStart() + 27;
    }

    private int getUseRowStart() {
        return this.getInventorySlotEnd();
    }

    private int getUseRowEnd() {
        return this.getUseRowStart() + 9;
    }
}
