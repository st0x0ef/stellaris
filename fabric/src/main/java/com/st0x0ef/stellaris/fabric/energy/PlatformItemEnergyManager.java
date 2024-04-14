package com.st0x0ef.stellaris.fabric.energy;

import com.st0x0ef.stellaris.common.energy.ItemStackHolder;
import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.energy.impl.SimpleEnergySnapshot;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

public record PlatformItemEnergyManager(ItemStackHolder holder, ContainerItemContext context,
                                        EnergyStorage energy) implements EnergyContainer {

    @Nullable
    public static PlatformItemEnergyManager of(ItemStackHolder stack) {
        ContainerItemContext context = ItemStackStorage.of(stack.getStack());
        var fabricEnergy = EnergyStorage.ITEM.find(stack.getStack(), context);
        return fabricEnergy == null ? null : new PlatformItemEnergyManager(stack, context, fabricEnergy);
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long insert = energy.insert(maxAmount, txn);
            if (simulate) txn.abort();
            else {
                txn.commit();
                holder.setStack(context.getItemVariant().toStack());
            }
            return insert;
        }
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long extract = energy.extract(maxAmount, txn);
            if (simulate) txn.abort();
            else {
                txn.commit();
                holder.setStack(context.getItemVariant().toStack());
            }
            return extract;
        }
    }

    @Override
    public void setEnergy(long energy) {
        try (Transaction txn = Transaction.openOuter()) {
            if (energy > this.energy.getAmount()) {
                this.energy.insert(energy - this.energy.getAmount(), txn);
            } else if (energy < this.energy.getAmount()) {
                this.energy.extract(this.energy.getAmount() - energy, txn);
            }
            txn.commit();
            holder.setStack(context.getItemVariant().toStack());
        }
    }

    @Override
    public long getStoredEnergy() {
        return energy.getAmount();
    }

    @Override
    public long getMaxEnergyStored() {
            return energy.getCapacity();
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
        try (Transaction txn = Transaction.openOuter()) {
            energy.extract(energy.getAmount(), txn);
            txn.commit();
            holder.setStack(context.getItemVariant().toStack());
        }
    }
}
