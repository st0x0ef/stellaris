package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.systems.energy.base.EnergyBlock;
import com.st0x0ef.stellaris.common.systems.energy.impl.SimpleEnergyContainer;
import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.st0x0ef.stellaris.common.blocks.entities.machines.BaseEnergyContainerBlockEntity.ENERGY_TAG;

public abstract class BaseEnergyBlockEntity extends BlockEntity implements EnergyBlock<WrappedBlockEnergyContainer>, WrappedEnergyBlockEntity, TickingBlockEntity {

    private WrappedBlockEnergyContainer energyContainer;

    public BaseEnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract int getMaxCapacity();

    @Override
    public WrappedBlockEnergyContainer getEnergyStorage(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return energyContainer == null ? energyContainer = new WrappedBlockEnergyContainer(entity, new SimpleEnergyContainer(getMaxCapacity(), Integer.MAX_VALUE)) : energyContainer;
    }

    @Override
    public WrappedBlockEnergyContainer getWrappedEnergyContainer() {
        return getEnergyStorage(getLevel(), getBlockPos(), getBlockState(), this, null);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        getWrappedEnergyContainer().setEnergy(tag.getLong(ENERGY_TAG));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putLong(ENERGY_TAG, getWrappedEnergyContainer().getStoredEnergy());
    }
}