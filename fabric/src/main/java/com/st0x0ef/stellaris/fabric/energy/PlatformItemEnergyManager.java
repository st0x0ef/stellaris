package com.st0x0ef.stellaris.fabric.energy;

import com.st0x0ef.stellaris.common.energy.ItemStackHolder;
import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.energy.impl.SimpleEnergySnapshot;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

public record PlatformItemEnergyManager(ItemStackHolder holder, ContainerItemContext context, EnergyStorage energy) implements EnergyContainer {
    public PlatformItemEnergyManager(ItemStackHolder holder, ContainerItemContext context, EnergyStorage energy) {
        this.holder = holder;
        this.context = context;
        this.energy = energy;
    }

    public static @Nullable PlatformItemEnergyManager of(ItemStackHolder stack) {
        ContainerItemContext context = ItemStackStorage.of(stack.getStack());
        EnergyStorage fabricEnergy = EnergyStorage.ITEM.find(stack.getStack(), context);
        return fabricEnergy == null ? null : new PlatformItemEnergyManager(stack, context, fabricEnergy);
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
                this.holder.setStack(this.context.getItemVariant().toStack());
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
                this.holder.setStack(this.context.getItemVariant().toStack());
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
        Transaction txn = Transaction.openOuter();

        try {
            if (energy > this.energy.getAmount()) {
                this.energy.insert(energy - this.energy.getAmount(), txn);
            } else if (energy < this.energy.getAmount()) {
                this.energy.extract(this.energy.getAmount() - energy, txn);
            }

            txn.commit();
            this.holder.setStack(this.context.getItemVariant().toStack());
        } catch (Throwable var7) {
            if (txn != null) {
                try {
                    txn.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }

            throw var7;
        }

        if (txn != null) {
            txn.close();
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
        Transaction txn = Transaction.openOuter();

        try {
            this.energy.extract(this.energy.getAmount(), txn);
            txn.commit();
            this.holder.setStack(this.context.getItemVariant().toStack());
        } catch (Throwable var5) {
            if (txn != null) {
                try {
                    txn.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }

            throw var5;
        }

        if (txn != null) {
            txn.close();
        }

    }

    public ItemStackHolder holder() {
        return this.holder;
    }

    public ContainerItemContext context() {
        return this.context;
    }

    public EnergyStorage energy() {
        return this.energy;
    }
}
