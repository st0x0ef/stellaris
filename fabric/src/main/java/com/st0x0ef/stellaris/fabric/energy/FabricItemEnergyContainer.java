package com.st0x0ef.stellaris.fabric.energy;

import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.energy.util.Updatable;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

public class FabricItemEnergyContainer<T extends EnergyContainer & Updatable> extends SnapshotParticipant<EnergySnapshot> implements EnergyStorage {
    private final ContainerItemContext ctx;
    private final ItemStack stack;
    private final T container;

    public FabricItemEnergyContainer(ContainerItemContext ctx, ItemStack stack, T container) {
        this.ctx = ctx;
        this.stack = stack;
        //CompoundTag nbt = ctx.getItemVariant().getNbt();
        //if (nbt != null) container.deserialize(nbt); // TODO : add a DataComponent for energy type
        this.container = container;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        this.updateSnapshots(transaction);
        long inserted = container.insertEnergy(maxAmount, false);
        setChanged(transaction);
        return inserted;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        this.updateSnapshots(transaction);
        long l = container.extractEnergy(maxAmount, false);
        setChanged(transaction);
        return l;
    }

    @Override
    public long getAmount() {
        return container.getStoredEnergy();
    }

    @Override
    public long getCapacity() {
        return container.getMaxCapacity();
    }

    public void setChanged(TransactionContext transaction) {
        this.container.update();
        ctx.exchange(ItemVariant.of(stack), ctx.getAmount(), transaction);
    }

    @Override
    protected EnergySnapshot createSnapshot() {
        return this.container.createSnapshot();
    }

    @Override
    protected void readSnapshot(EnergySnapshot snapshot) {
        this.container.readSnapshot(snapshot);
    }
}
