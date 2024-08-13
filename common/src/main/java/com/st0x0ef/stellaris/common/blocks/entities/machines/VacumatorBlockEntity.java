package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.items.CanItem;
import com.st0x0ef.stellaris.common.menus.VacumatorMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VacumatorBlockEntity extends BaseContainerBlockEntity implements ImplementedInventory, TickingBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);

    public VacumatorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.VACUMATOR_ENTITY.get(), blockPos, blockState);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new VacumatorMenu(i, inventory, this);
    }

    @Override
    public void setChanged() {
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        super.setChanged();
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag) {
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
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.vacumator");
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction direction) {
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

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void tick() {
        if (canCraft()) {
            craft();
            setChanged();
        }
    }

    public boolean canCraft() {
        if (getItem(0).getItem() instanceof CanItem) {
            return isFood(getItem(1)) && getItem(2).is(Items.GLASS_BOTTLE) && getItem(3).isEmpty() && getItem(4).isEmpty();
        }

        return false;
    }

    public void craft() {
        ItemStack canStack = getItem(0);
        ItemStack resultStack = new ItemStack(canStack.getItem());
        CanItem.setFoodProperties(resultStack.getItem(), CanItem.getFoodProperties(canStack.getItem()));

        if (CanItem.addFoodToCan(resultStack, getItem(1))) {
            for (int i = 0; i < 3; i++) {
                removeItem(i, 1);
            }

            setItem(3, resultStack);
            setItem(4, Items.POTION.getDefaultInstance());
        }
    }

    public static boolean isFood(ItemStack food) {
        return food.isEdible();
    }

    @Override
    public int getContainerSize() {
        return 5;
    }
}