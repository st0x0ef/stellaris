package com.st0x0ef.stellaris.common.energy.impl;


import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.energy.util.Updatable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/**
 * Represents a wrapped energy container for an item.
 * This class implements the EnergyContainer interface and the Updatable interface.
 * It delegates energy-related operations to the wrapped energy container, and updates the item when the energy is changed.
 *
 * @param stack     The item stack.
 * @param container The wrapped energy container. Botarium provides a default implementation for this with {@link SimpleEnergyContainer}.
 */
public record WrappedItemEnergyContainer(ItemStack stack, EnergyContainer container) implements EnergyContainer, Updatable {
    @Override
    public long insertEnergy(long energy, boolean simulate) {
        return container.insertEnergy(energy, simulate);
    }

    @Override
    public long extractEnergy(long energy, boolean simulate) {
        return container.extractEnergy(energy, simulate);
    }

    @Override
    public long internalInsert(long amount, boolean simulate) {
        long l = container.internalInsert(amount, simulate);
        if (!simulate) update();
        return l;
    }

    @Override
    public long internalExtract(long amount, boolean simulate) {
        long extracted = container.internalExtract(amount, simulate);
        if (!simulate) update();
        return extracted;
    }

    @Override
    public void setEnergy(long energy) {
        container.setEnergy(energy);
    }

    @Override
    public long getStoredEnergy() {
        return container.getStoredEnergy();
    }

    @Override
    public long getMaxEnergyStored() {
        return container.getMaxEnergyStored();
    }

    @Override
    public long getMaxCapacity() {
        return container.getMaxCapacity();
    }

    @Override
    public long maxInsert() {
        return container.maxInsert();
    }

    @Override
    public long maxExtract() {
        return container.maxExtract();
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
    public EnergySnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    public void deserialize(CompoundTag nbt, HolderLookup.Provider provider) {
        container.deserialize(nbt, provider);
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt, HolderLookup.Provider provider) {
        return container.serialize(nbt, provider);
    }

    @Override
    public void update() {
        //container.serialize(stack.getOrCreateTag()); // TODO : find how to fix that
    }

    @Override
    public void clearContent() {
        container.clearContent();
        update();
    }
}
