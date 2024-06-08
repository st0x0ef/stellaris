package com.st0x0ef.stellaris.common.systems.fluid.impl;

import com.st0x0ef.stellaris.common.systems.fluid.base.FluidContainer;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidSnapshot;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public record UnlimitedFluidContainer(Fluid fluidType) implements FluidContainer, Updatable {

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        return 0;
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        return fluid.is(fluidType) ? fluid : FluidHolder.empty();
    }

    @Override
    public void setFluid(int slot, FluidHolder fluid) {
        // do nothing
    }

    @Override
    public List<FluidHolder> getFluids() {
        return List.of(FluidHolder.of(fluidType, Integer.MAX_VALUE, null));
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public FluidContainer copy() {
        return new UnlimitedFluidContainer(fluidType);
    }

    @Override
    public long getTankCapacity(int tankSlot) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void fromContainer(FluidContainer container) {
        // do nothing
    }

    @Override
    public long extractFromSlot(FluidHolder fluidHolder, FluidHolder toInsert, Runnable snapshot) {
        return fluidHolder.is(fluidType) ? fluidHolder.getFluidAmount() : 0;
    }

    @Override
    public boolean allowsInsertion() {
        return false;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return new SimpleFluidSnapshot(this);
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        // do nothing
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return nbt;
    }

    @Override
    public void update() {
        // do nothing
    }

    @Override
    public void clearContent() {
        // do nothing
    }
}
