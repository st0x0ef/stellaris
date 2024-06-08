package com.st0x0ef.stellaris.fabric.systems.fluid.storage;

import com.st0x0ef.stellaris.common.systems.fluid.base.FluidContainer;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidSnapshot;
import com.st0x0ef.stellaris.common.systems.fluid.impl.SimpleFluidSnapshot;
import com.st0x0ef.stellaris.fabric.systems.fluid.holder.FabricFluidHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record PlatformFluidContainer(Storage<FluidVariant> storage) implements FluidContainer {

    public static PlatformFluidContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(level, pos, state, entity, direction);
        return storage == null ? null : new PlatformFluidContainer(storage);
    }

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            FabricFluidHolder fabricFluidHolder = FabricFluidHolder.of(fluid);
            long inserted = storage.insert(fabricFluidHolder.toVariant(), fabricFluidHolder.getAmount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            FabricFluidHolder fabricFluidHolder = FabricFluidHolder.of(fluid);
            long extracted = storage.extract(fabricFluidHolder.toVariant(), fabricFluidHolder.getAmount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted == 0 ? FabricFluidHolder.of(fabricFluidHolder.toVariant(), extracted) : fluid;
        }
    }

    @Override
    public void setFluid(int slot, FluidHolder fluid) {
        throw new UnsupportedOperationException("You may not set a fluid in a PlatformFluidContainer");
    }

    @Override
    public List<FluidHolder> getFluids() {
        List<FluidHolder> fluids = new ArrayList<>();
        storage.iterator().forEachRemaining(variant -> fluids.add(FabricFluidHolder.of(variant.getResource(), variant.getAmount())));
        return fluids;
    }

    @Override
    public int getSize() {
        return getFluids().size();
    }

    @Override
    public boolean isEmpty() {
        return getFluids().isEmpty() || getFluids().stream().allMatch(FluidHolder::isEmpty);
    }

    @Override
    public FluidContainer copy() {
        throw new UnsupportedOperationException("You may not copy a PlatformFluidContainer");
    }

    @Override
    public long getTankCapacity(int tank) {
        List<StorageView<FluidVariant>> fluids = new ArrayList<>();
        storage.iterator().forEachRemaining(fluids::add);
        return fluids.get(tank).getCapacity();
    }

    @Override
    public void fromContainer(FluidContainer container) {
        throw new UnsupportedOperationException("You may not overwrite a PlatformFluidContainer");
    }

    @Override
    public long extractFromSlot(FluidHolder fluidHolder, FluidHolder toInsert, Runnable snapshot) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long extractFromSlot(int slot, FluidHolder toExtract, boolean simulate) {
        if (storage instanceof SlottedStorage<FluidVariant> slottedStorage) {
            try (Transaction transaction = Transaction.openOuter()) {
                SingleSlotStorage<FluidVariant> fluidSlot = slottedStorage.getSlot(slot);
                long extracted = fluidSlot.extract(FabricFluidHolder.of(toExtract).getResource(), toExtract.getFluidAmount(), transaction);
                if (!simulate) {
                    transaction.commit();
                }
                return extracted;
            }
        }
        return extractFluid(toExtract, simulate).getFluidAmount();
    }

    @Override
    public boolean allowsInsertion() {
        return storage.supportsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return storage.supportsExtraction();
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return new SimpleFluidSnapshot(this);
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
        storage.iterator().forEachRemaining(variant -> storage.extract(variant.getResource(), variant.getAmount(), Transaction.openOuter()));
    }
}
