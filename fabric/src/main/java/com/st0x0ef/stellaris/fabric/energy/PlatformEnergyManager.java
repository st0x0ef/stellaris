package com.st0x0ef.stellaris.fabric.energy;

import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.energy.impl.SimpleEnergySnapshot;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

@ApiStatus.Internal
public record PlatformEnergyManager(EnergyStorage energy) implements EnergyContainer {
    public PlatformEnergyManager(EnergyStorage energy) {
        this.energy = energy;
    }

    public static @Nullable PlatformEnergyManager of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        EnergyStorage fabricEnergy = EnergyStorage.SIDED.find(level, pos, state, entity, direction);
        return fabricEnergy == null ? null : new PlatformEnergyManager(fabricEnergy);
    }

    public long insertEnergy(long maxAmount, boolean simulate) {
        Transaction txn = Transaction.openOuter();

        long var7;
        try {
            long insert = this.energy.insert(maxAmount, txn);
            if (simulate) {
                txn.abort();
            } else {
                txn.commit();
            }

            var7 = insert;
        } catch (Throwable var10) {
            if (txn != null) {
                try {
                    txn.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }
            }

            throw var10;
        }

        if (txn != null) {
            txn.close();
        }

        return var7;
    }

    public long extractEnergy(long maxAmount, boolean simulate) {
        Transaction txn = Transaction.openOuter();

        long var7;
        try {
            long extract = this.energy.extract(maxAmount, txn);
            if (simulate) {
                txn.abort();
            } else {
                txn.commit();
            }

            var7 = extract;
        } catch (Throwable var10) {
            if (txn != null) {
                try {
                    txn.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }
            }

            throw var10;
        }

        if (txn != null) {
            txn.close();
        }

        return var7;
    }

    public void setEnergy(long energy) {
        if (energy > this.energy.getAmount()) {
            this.insertEnergy(energy - this.energy.getAmount(), false);
        } else if (energy < this.energy.getAmount()) {
            this.extractEnergy(this.energy.getAmount() - energy, false);
        }

    }

    public long getStoredEnergy() {
        return this.energy.getAmount();
    }

    public long getMaxCapacity() {
        return this.energy.getCapacity();
    }

    public long maxInsert() {
        return this.energy.getCapacity();
    }

    public long maxExtract() {
        return this.energy.getCapacity();
    }

    public boolean allowsInsertion() {
        return this.energy.supportsInsertion();
    }

    public boolean allowsExtraction() {
        return this.energy.supportsExtraction();
    }

    public EnergySnapshot createSnapshot() {
        return new SimpleEnergySnapshot(this);
    }

    public void deserialize(CompoundTag nbt, HolderLookup.Provider provider) {

    }

    public CompoundTag serialize(CompoundTag nbt, HolderLookup.Provider provider) {
        return nbt;
    }

    public void clearContent() {
        this.setEnergy(0L);
    }

    public EnergyStorage energy() {
        return this.energy;
    }
}
