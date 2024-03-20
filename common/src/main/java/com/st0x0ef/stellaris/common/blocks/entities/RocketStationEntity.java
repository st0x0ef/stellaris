package com.st0x0ef.stellaris.common.blocks.entities;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.recipes.RocketStationRecipe;
import com.st0x0ef.stellaris.common.menus.RocketStationMenu;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class RocketStationEntity extends BaseContainerBlockEntity implements ImplementedInventory {

    private NonNullList<ItemStack> items;
    private List<Integer> inputSlots = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);


    public RocketStationEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.ROCKET_STATION.get(), blockPos, blockState);

        this.items = NonNullList.withSize(15, ItemStack.EMPTY);

    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Rocket Station");
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new RocketStationMenu(i, inventory, this);
    }

    @Override
    public void setChanged() {
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        super.setChanged();
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items);
    }


    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<RocketStationRecipe>> recipe = this.getCurrentRecipe();

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(recipe.get().value().getResultItem(null))
                && canInsertItemIntoOutputSlot(recipe.get().value().getResultItem(null).getItem());
    }

    private Optional<RecipeHolder<RocketStationRecipe>> getCurrentRecipe() {
        SimpleContainer simpleContainer = new SimpleContainer(this.getContainerSize());
        for (int i = 0; i < this.getContainerSize(); i++) {
            simpleContainer.setItem(i, this.getItem(i));
        }
       return getLevel().getRecipeManager().getRecipeFor(RocketStationRecipe.Type.INSTANCE, simpleContainer, getLevel());
    }

    private void craftItem() {
        Optional<RecipeHolder<RocketStationRecipe>> recipe = getCurrentRecipe();
        Stellaris.LOG.error("TEST Slot Count: " + getItem(0).getCount());

        for (int i : inputSlots) {
            this.removeItem(i, 1);
        }
        this.setItem(14, new ItemStack(recipe.get().value().getResultItem(null).getItem(),
                getItem(14).getCount() + recipe.get().value().getResultItem(null).getCount()));
    }


    public void tick(Level world, BlockPos pos, BlockState state) {
        if(world.isClientSide()) {
            return;
        }

        if(isOutputSlotEmptyOrReceivable()) {
            if(this.hasRecipe()) {
                setChanged(world, pos, state);
                this.craftItem();

            }
        } else {
            setChanged(world, pos, state);
        }
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getItem(14).getItem() == item || this.getItem(14).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getItem(14).getCount() + result.getCount() <= getItem(14).getMaxStackSize();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getItem(14).isEmpty() || this.getItem(14).getCount() < this.getItem(14).getMaxStackSize();
    }
}