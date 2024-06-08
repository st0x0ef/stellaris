package com.st0x0ef.stellaris.common.systems.fluid.impl;

import com.st0x0ef.stellaris.common.systems.fluid.base.FluidContainer;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidSnapshot;
import com.st0x0ef.stellaris.common.systems.fluid.base.ItemFluidContainer;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Represents a wrapped fluid container for an item.
 * This class implements the FluidContainer interface and the Updatable interface.
 * It delegates fluid-related operations to the wrapped fluid container, and updates the item when the fluid is changed.
 *
 * @param stack     The item stack.
 * @param container The wrapped fluid container. Botarium provides a default implementation for this with {@link SimpleFluidContainer}.
 */
public record WrappedItemFluidContainer(ItemStack stack,
                                        FluidContainer container) implements ItemFluidContainer, Updatable {

    public WrappedItemFluidContainer {
//        container.deserialize(stack.getOrCreateTag()); TODO fix this
    }

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        return container.insertFluid(fluid, simulate);
    }

    @Override
    public long internalInsert(FluidHolder fluids, boolean simulate) {
        long inserted = container.internalInsert(fluids, simulate);
        if (!simulate) update();
        return inserted;
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        return container.extractFluid(fluid, simulate);
    }

    @Override
    public FluidHolder internalExtract(FluidHolder fluid, boolean simulate) {
        FluidHolder extracted = container.internalExtract(fluid, simulate);
        if (!simulate) update();
        return extracted;
    }

    @Override
    public void setFluid(int slot, FluidHolder fluid) {
        container.setFluid(slot, fluid);
        update();
    }

    @Override
    public List<FluidHolder> getFluids() {
        return container.getFluids();
    }

    @Override
    public int getSize() {
        return container.getSize();
    }

    @Override
    public boolean isEmpty() {
        return container.isEmpty();
    }

    @Override
    public FluidContainer copy() {
        return container.copy();
    }

    @Override
    public long getTankCapacity(int tankSlot) {
        return container.getTankCapacity(tankSlot);
    }

    @Override
    public void fromContainer(FluidContainer container) {
        this.container.fromContainer(container);
    }

    @Override
    public long extractFromSlot(FluidHolder fluidHolder, FluidHolder toInsert, Runnable snapshot) {
        return container.extractFromSlot(fluidHolder, toInsert, snapshot);
    }

    @Override
    public long extractFromSlot(int slot, FluidHolder toExtract, boolean simulate) {
        return container.extractFromSlot(slot, toExtract, simulate);
    }

    @Override
    public boolean allowsInsertion() {
        return container.allowsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return container.allowsExtraction();
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    public void readSnapshot(FluidSnapshot snapshot) {
        container.readSnapshot(snapshot);
    }

    @Override
    public boolean isFluidValid(int slot, FluidHolder fluidHolder) {
        return container.isFluidValid(slot, fluidHolder);
    }

    @Override
    public ItemStack getContainerItem() {
        return stack;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        container.deserialize(nbt);
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return container.serialize(nbt);
    }

    @Override
    public void update() {
        serialize(stack.getOrCreateTag());
    }

    @Override
    public void clearContent() {
        container.clearContent();
    }
}
