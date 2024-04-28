package com.st0x0ef.stellaris.neoforge.energy;

import com.st0x0ef.stellaris.common.energy.ItemStackHolder;
import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.energy.impl.SimpleEnergySnapshot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public record PlatformEnergyManager(IEnergyStorage energy) implements EnergyContainer {

    @Nullable
    public static EnergyContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        var energy = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, entity, direction);
        return energy != null ? new PlatformEnergyManager(energy) : null;
    }

    @Nullable
    public static EnergyContainer of(ItemStackHolder stack) {
        var energy = stack.getStack().getCapability(Capabilities.EnergyStorage.ITEM);
        return energy != null ? new PlatformEnergyManager(energy) : null;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        return energy.receiveEnergy((int) maxAmount, simulate);
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        return energy.extractEnergy((int) maxAmount, simulate);
    }

    @Override
    public void setEnergy(long energy) {
        if (energy > this.energy.getEnergyStored()) {
            this.energy.receiveEnergy((int) (energy - this.energy.getEnergyStored()), false);
        } else if (energy < this.energy.getEnergyStored()) {
            this.energy.extractEnergy((int) (this.energy.getEnergyStored() - energy), false);
        }
    }

    @Override
    public long getStoredEnergy() {
        return energy.getEnergyStored();
    }

    @Override
    public long getMaxEnergyStored() {
        return 0;
    }

    @Override
    public long getMaxCapacity() {
        return energy.getMaxEnergyStored();
    }

    @Override
    public long maxInsert() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long maxExtract() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean allowsInsertion() {
        return energy.canReceive();
    }

    @Override
    public boolean allowsExtraction() {
        return energy.canExtract();
    }

    @Override
    public EnergySnapshot createSnapshot() {
        return new SimpleEnergySnapshot(this);
    }

    @Override
    public void clearContent() {
        setEnergy(0);
    }

    @Override
    public void deserialize(CompoundTag nbt, HolderLookup.Provider provider) {

    }

    @Override
    public CompoundTag serialize(CompoundTag nbt, HolderLookup.Provider provider) {
        return nbt;
    }
}
