package com.st0x0ef.stellaris.common.systems.fluid.impl;

import com.st0x0ef.stellaris.common.systems.SystemsMain;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidContainer;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidSnapshot;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.IntToLongFunction;

/**
 * A simple implementation for fluid storage.
 * This class should be wrapped by a {@link WrappedBlockFluidContainer} or a {@link WrappedItemFluidContainer} to provide the necessary functionality.
 */
public class SimpleFluidContainer implements FluidContainer {
    public static final String FLUID_KEY = "StoredFluids";

    public NonNullList<FluidHolder> storedFluid;
    public final IntToLongFunction maxAmount;
    public final BiPredicate<Integer, FluidHolder> fluidFilter;

    public SimpleFluidContainer(IntToLongFunction maxAmount, int tanks, BiPredicate<Integer, FluidHolder> fluidFilter) {
        this.maxAmount = maxAmount;
        this.fluidFilter = fluidFilter;
        this.storedFluid = NonNullList.withSize(tanks, FluidHolder.empty());
    }

    public SimpleFluidContainer(long maxAmount, int tanks, BiPredicate<Integer, FluidHolder> fluidFilter) {
        this(integer -> maxAmount, tanks, fluidFilter);
    }

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        for (int i = 0; i < this.storedFluid.size(); i++) {
            if (fluidFilter.test(i, fluid)) {
                if (storedFluid.get(i).isEmpty()) {
                    FluidHolder insertedFluid = fluid.copyHolder();
                    insertedFluid.setAmount((long) Mth.clamp(fluid.getFluidAmount(), 0, maxAmount.applyAsLong(i)));
                    if (simulate) return insertedFluid.getFluidAmount();
                    this.storedFluid.set(i, insertedFluid);
                    return storedFluid.get(i).getFluidAmount();
                } else {
                    if (storedFluid.get(i).matches(fluid)) {
                        long insertedAmount = (long) Mth.clamp(fluid.getFluidAmount(), 0, maxAmount.applyAsLong(i) - storedFluid.get(i).getFluidAmount());
                        if (simulate) return insertedAmount;
                        this.storedFluid.get(i).setAmount(storedFluid.get(i).getFluidAmount() + insertedAmount);
                        return insertedAmount;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        for (int i = 0; i < this.storedFluid.size(); i++) {
            if (fluidFilter.test(i, fluid)) {
                FluidHolder toExtract = fluid.copyHolder();
                if (storedFluid.isEmpty()) {
                    return FluidHolder.empty();
                } else if (storedFluid.get(i).matches(fluid)) {
                    long extractedAmount = (long) Mth.clamp(fluid.getFluidAmount(), 0, storedFluid.get(i).getFluidAmount());
                    toExtract.setAmount(extractedAmount);
                    if (simulate) return toExtract;
                    this.storedFluid.get(i).setAmount(storedFluid.get(i).getFluidAmount() - extractedAmount);
                    if (storedFluid.get(i).getFluidAmount() == 0) storedFluid.set(i, FluidHolder.empty());
                    return toExtract;
                }
            }
        }
        return FluidHolder.empty();
    }

    @Override
    public long internalInsert(FluidHolder fluid, boolean simulate) {
        return insertFluid(fluid, simulate);
    }

    @Override
    public FluidHolder internalExtract(FluidHolder fluid, boolean simulate) {
        return extractFluid(fluid, simulate);
    }

    public long extractFromSlot(FluidHolder fluidHolder, FluidHolder toExtract, Runnable snapshot) {
        if (fluidHolder.matches(toExtract) && !fluidHolder.isEmpty()) {
            long extracted = Mth.clamp(toExtract.getFluidAmount(), 0, fluidHolder.getFluidAmount());
            snapshot.run();
            fluidHolder.setAmount(fluidHolder.getFluidAmount() - extracted);
            if (fluidHolder.getFluidAmount() == 0) fluidHolder.setFluid(Fluids.EMPTY);
            return extracted;
        }
        return 0;
    }

    @Override
    public long extractFromSlot(int slot, FluidHolder toExtract, boolean simulate) {
        if (slot < 0 || slot >= this.storedFluid.size()) return 0;
        FluidHolder fluidHolder = this.storedFluid.get(slot);
        if (!fluidHolder.isEmpty() && fluidHolder.matches(toExtract)) {
            long extracted = Mth.clamp(toExtract.getFluidAmount(), 0, fluidHolder.getFluidAmount());
            if (!simulate) {
                fluidHolder.setAmount(fluidHolder.getFluidAmount() - extracted);
                if (fluidHolder.getFluidAmount() == 0) fluidHolder.setFluid(Fluids.EMPTY);
                this.storedFluid.set(slot, fluidHolder);
            }
            return extracted;
        }
        return 0;
    }

    @Override
    public void setFluid(int slot, FluidHolder fluid) {
        this.storedFluid.set(slot, fluid);
    }

    @Override
    public List<FluidHolder> getFluids() {
        return storedFluid;
    }

    @Override
    public int getSize() {
        return getFluids().size();
    }

    @Override
    public boolean isEmpty() {
        return getFluids().isEmpty() || getFluids().get(0) == null || getFluids().get(0).isEmpty();
    }

    @Override
    public SimpleFluidContainer copy() {
        return new SimpleFluidContainer(maxAmount, this.getSize(), fluidFilter);
    }

    @Override
    public long getTankCapacity(int slot) {
        return this.maxAmount.applyAsLong(slot);
    }

    @Override
    public void fromContainer(FluidContainer container) {
        this.storedFluid = NonNullList.withSize(container.getSize(), FluidHolder.empty());
        for (int i = 0; i < container.getSize(); i++) {
            this.storedFluid.set(i, container.getFluids().get(i).copyHolder());
        }
    }

    @Override
    public void deserialize(CompoundTag root) {
        CompoundTag tag = root.getCompound(SystemsMain.SYSTEMS_DATA);
        ListTag fluids = tag.getList(FLUID_KEY, Tag.TAG_COMPOUND);
        for (int i = 0; i < fluids.size(); i++) {
            CompoundTag fluid = fluids.getCompound(i);
            this.storedFluid.set(i, FluidHolder.fromCompound(fluid));
        }
    }

    @Override
    public CompoundTag serialize(CompoundTag root) {
        CompoundTag tag = root.getCompound(SystemsMain.SYSTEMS_DATA);
        if (!this.storedFluid.isEmpty()) {
            ListTag tags = new ListTag();
            for (FluidHolder fluidHolder : this.storedFluid) {
                tags.add(fluidHolder.serialize());
            }
            tag.put(FLUID_KEY, tags);
        } else {
            tag.put(FLUID_KEY, new ListTag());
        }
        root.put(SystemsMain.SYSTEMS_DATA, tag);
        return root;
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }

    @Override
    public boolean isFluidValid(int slot, FluidHolder fluidHolder) {
        return fluidFilter.test(slot, fluidHolder);
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return new SimpleFluidSnapshot(this);
    }

    @Override
    public void clearContent() {
        this.storedFluid.clear();
    }
}