package com.st0x0ef.stellaris.fabric.energy;

import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.energy.util.Updatable;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

public class FabricItemEnergyContainer<T extends EnergyContainer & Updatable> extends SnapshotParticipant<EnergySnapshot> implements EnergyStorage {
    private final ContainerItemContext ctx;
    private final ItemStack stack;
    private final T container;

    public FabricItemEnergyContainer(ContainerItemContext ctx, ItemStack stack, T container) {
        this.ctx = ctx;
        this.stack = stack;
        //CompoundTag nbt = ctx.getItemVariant().getNbt(); // TODO : add a DataComponent for energy typ
        //if (nbt != null) {
        //    container.deserialize(nbt);
        //}

        this.container = container;
    }

    public long insert(long maxAmount, TransactionContext transaction) {
        this.updateSnapshots(transaction);
        long inserted = this.container.insertEnergy(maxAmount, false);
        this.setChanged(transaction);
        return inserted;
    }

    public long extract(long maxAmount, TransactionContext transaction) {
        this.updateSnapshots(transaction);
        long l = this.container.extractEnergy(maxAmount, false);
        this.setChanged(transaction);
        return l;
    }

    public long getAmount() {
        return this.container.getStoredEnergy();
    }

    public long getCapacity() {
        return this.container.getMaxCapacity();
    }

    public void setChanged(TransactionContext transaction) {
        this.container.update();
        this.ctx.exchange(ItemVariant.of(this.stack), this.ctx.getAmount(), transaction);
    }

    protected EnergySnapshot createSnapshot() {
        return this.container.createSnapshot();
    }

    protected void readSnapshot(EnergySnapshot snapshot) {
        this.container.readSnapshot(snapshot);
    }
}
