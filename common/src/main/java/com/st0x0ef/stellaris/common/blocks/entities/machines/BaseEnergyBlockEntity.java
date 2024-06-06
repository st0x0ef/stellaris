package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.systems.energy.base.EnergyBlock;
import com.st0x0ef.stellaris.common.systems.energy.impl.SimpleEnergyContainer;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseEnergyBlockEntity extends BaseContainerBlockEntity implements EnergyBlock<WrappedBlockEnergyContainer> {
    private WrappedBlockEnergyContainer energyContainer;
    private final String tagName;
    protected NonNullList<ItemStack> items;

    public BaseEnergyBlockEntity(BlockEntityType<?> entityType, BlockPos blockPos,
                                 BlockState blockState, String tagName, int containerSize) {
        super(entityType, blockPos, blockState);
        this.tagName = tagName;
        this.items = NonNullList.withSize(containerSize, ItemStack.EMPTY);
    }

    @Override
    public WrappedBlockEnergyContainer getEnergyStorage(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return energyContainer == null ? energyContainer = new WrappedBlockEnergyContainer(entity, new SimpleEnergyContainer(15000, Integer.MAX_VALUE)) : energyContainer;
    }
    public abstract WrappedBlockEnergyContainer getWrappedEnergyContainer();

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, provider);
        return tag;
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        getWrappedEnergyContainer().setEnergy(compoundTag.getLong(tagName));
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putLong(this.tagName, getWrappedEnergyContainer().getStoredEnergy());
    }

    @Override
    protected abstract Component getDefaultName();

    @Override
    protected abstract NonNullList<ItemStack> getItems();

    @Override
    protected abstract void setItems(NonNullList<ItemStack> items);

    @Override
    public abstract int getContainerSize();

    public void tick() {
    }
}