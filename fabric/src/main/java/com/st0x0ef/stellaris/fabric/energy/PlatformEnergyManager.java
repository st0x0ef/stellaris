package com.st0x0ef.stellaris.fabric.energy;

import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.energy.impl.SimpleEnergySnapshot;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

@ApiStatus.Internal
public record PlatformEnergyManager(EnergyStorage energy) implements EnergyContainer {

    @Nullable
    public static PlatformEnergyManager of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        EnergyStorage fabricEnergy = EnergyStorage.SIDED.find(level, pos, state, entity, direction);
        return fabricEnergy == null ? null : new PlatformEnergyManager(fabricEnergy);
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long insert = energy.insert(maxAmount, txn);
            if (simulate) txn.abort();
            else txn.commit();
            return insert;
        }
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long extract = energy.extract(maxAmount, txn);
            if (simulate) txn.abort();
            else txn.commit();
            return extract;
        }
    }

    @Override
    public void setEnergy(long energy) {
        if (energy > this.energy.getAmount()) {
            insertEnergy(energy - this.energy.getAmount(), false);
        } else if (energy < this.energy.getAmount()) {
            extractEnergy(this.energy.getAmount() - energy, false);
        }
    }

    @Override
    public long getStoredEnergy() {
        return energy.getAmount();
    }

    @Override
    public long getMaxCapacity() {
        return energy.getCapacity();
    }

    @Override
    public long maxInsert() {
        return energy.getCapacity();
    }

    @Override
    public long maxExtract() {
        return energy.getCapacity();
    }

    @Override
    public boolean allowsInsertion() {
        return energy.supportsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return energy.supportsExtraction();
    }

    @Override
    public EnergySnapshot createSnapshot() {
        return new SimpleEnergySnapshot(this);
    }

    @Override
    public void deserialize(CompoundTag nbt) {

    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return nbt;
    }

    @Override
    public void clearContent() {
        setEnergy(0);
    }
}
