package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.data.recipes.RocketStationRecipe;
import com.st0x0ef.stellaris.common.menus.RocketStationMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RocketStationEntity extends BaseContainerBlockEntity implements ImplementedInventory {

    private NonNullList<ItemStack> items = NonNullList.withSize(15, ItemStack.EMPTY);
    private final RecipeManager.CachedCheck<RocketStationEntity, RocketStationRecipe> quickCheck = RecipeManager.createCheck(RecipesRegistry.ROCKET_STATION_TYPE.get());

    public RocketStationEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.ROCKET_STATION.get(), blockPos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.stellaris.rocket_station");
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
    public NonNullList<ItemStack> getItems() {
        return items;
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

    public void tick(Level level) {
        if (level.isClientSide()) {
            return;
        }

        ItemStack outputStack = getItem(14);
        if (outputStack.isEmpty() || outputStack.getCount() < outputStack.getMaxStackSize()) {
            Optional<RecipeHolder<RocketStationRecipe>> recipeHolder = quickCheck.getRecipeFor(this, level);
            if (recipeHolder.isPresent()) {
                RocketStationRecipe recipe = recipeHolder.get().value();
                ItemStack resultStack = recipe.getResultItem(level.registryAccess());
                if (outputStack.isEmpty() || (ItemStack.isSameItemSameTags(outputStack, resultStack)
                        && outputStack.getCount() + resultStack.getCount() <= outputStack.getMaxStackSize())) {

                    if (outputStack.isEmpty()) {
                        setItem(14, resultStack.copy());
                    }
                    else if (ItemStack.isSameItemSameTags(outputStack, resultStack)) {
                        outputStack.grow(1);
                    }
                    else return;

                    for (int i = 0; i < 14; i++) {
                        ItemStack stack = getItem(i);
                        stack.shrink(1);

                        if (stack.isEmpty()) {
                            setItem(i, ItemStack.EMPTY);
                        }
                    }
                    setChanged();
                }
            }
        }
    }

    @Override
    public int getContainerSize() {
        return 15;
    }
}