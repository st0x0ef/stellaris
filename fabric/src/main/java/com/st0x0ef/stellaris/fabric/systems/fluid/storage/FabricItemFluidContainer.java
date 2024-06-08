package com.st0x0ef.stellaris.fabric.systems.fluid.storage;

import com.st0x0ef.stellaris.common.systems.fluid.base.FluidSnapshot;
import com.st0x0ef.stellaris.common.systems.fluid.base.ItemFluidContainer;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import com.st0x0ef.stellaris.fabric.systems.fluid.holder.FabricFluidHolder;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class FabricItemFluidContainer<T extends ItemFluidContainer & Updatable> extends ExtendedFluidContainer implements Storage<FluidVariant> {
    protected final T container;
    private final ContainerItemContext ctx;

    public FabricItemFluidContainer(ContainerItemContext ctx, T container) {
        this.container = container;
        this.ctx = ctx;
        CompoundTag nbt = ctx.getItemVariant().getNbt();
        if (nbt != null) container.deserialize(nbt);
    }

    @Override
    public boolean supportsInsertion() {
        return container.allowsInsertion();
    }

    @Override
    public boolean supportsExtraction() {
        return container.allowsExtraction();
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        long inserted = container.insertFluid(FabricFluidHolder.of(resource, maxAmount), false);
        setChanged(transaction);
        return inserted;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        long extracted = container.extractFluid(FabricFluidHolder.of(resource, maxAmount), false).getFluidAmount();
        setChanged(transaction);
        return extracted;
    }

    @Override
    public @NotNull Iterator<StorageView<FluidVariant>> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < container.getSize();
            }

            @Override
            public StorageView<FluidVariant> next() {
                return new SingleItemFluidSlot(FabricItemFluidContainer.this, index++);
            }
        };
    }

    public void setChanged(TransactionContext transaction) {
        ItemStack stack = ctx.getItemVariant().toStack();
        //this.container.serialize(stack.getOrCreateTag()); TODO fix this
        ctx.exchange(ItemVariant.of(stack), ctx.getAmount(), transaction);
    }

    @Override
    public void onFinalCommit() {
        container.update();
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    public void readSnapshot(FluidSnapshot snapshot) {
        container.readSnapshot(snapshot);
    }
}
