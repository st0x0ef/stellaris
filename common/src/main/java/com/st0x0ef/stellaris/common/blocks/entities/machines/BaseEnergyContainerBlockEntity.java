package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.systems.energy.base.EnergyBlock;
import com.st0x0ef.stellaris.common.systems.energy.impl.SimpleEnergyContainer;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseEnergyContainerBlockEntity extends BaseContainerBlockEntity implements EnergyBlock<WrappedBlockEnergyContainer>, WrappedEnergyBlockEntity, ImplementedInventory, TickingBlockEntity {

    public static final String ENERGY_TAG = "stellaris.energy";

    private WrappedBlockEnergyContainer energyContainer;
    private NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);

    public BaseEnergyContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    protected int getMaxCapacity() {
        return 15000;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public WrappedBlockEnergyContainer getEnergyStorage(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return energyContainer == null ? energyContainer = new WrappedBlockEnergyContainer(entity, new SimpleEnergyContainer(getMaxCapacity(), Integer.MAX_VALUE)) : energyContainer;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        super.load(tag);
        getWrappedEnergyContainer().setEnergy(tag.getLong(ENERGY_TAG));
        ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putLong(ENERGY_TAG, getWrappedEnergyContainer().getStoredEnergy());
        ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    public WrappedBlockEnergyContainer getWrappedEnergyContainer() {
        return getEnergyStorage(level, worldPosition, getBlockState(), this, null);
    }
}