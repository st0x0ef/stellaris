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
    public WrappedItemEnergyContainer(ItemStack stack, EnergyContainer container) {
        container.deserialize(stack.getOrCreateTag());
        this.stack = stack;
        this.container = container;
    }

    public long insertEnergy(long energy, boolean simulate) {
        return this.container.insertEnergy(energy, simulate);
    }

    public long extractEnergy(long energy, boolean simulate) {
        return this.container.extractEnergy(energy, simulate);
    }

    public long internalInsert(long amount, boolean simulate) {
        long l = this.container.internalInsert(amount, simulate);
        if (!simulate) {
            this.update();
        }

        return l;
    }

    public long internalExtract(long amount, boolean simulate) {
        long extracted = this.container.internalExtract(amount, simulate);
        if (!simulate) {
            this.update();
        }

        return extracted;
    }

    public void setEnergy(long energy) {
        this.container.setEnergy(energy);
    }

    public long getStoredEnergy() {
        return this.container.getStoredEnergy();
    }

    public long getMaxCapacity() {
        return this.container.getMaxCapacity();
    }

    public long maxInsert() {
        return this.container.maxInsert();
    }

    public long maxExtract() {
        return this.container.maxExtract();
    }

    public boolean allowsInsertion() {
        return this.container.allowsInsertion();
    }

    public boolean allowsExtraction() {
        return this.container.allowsExtraction();
    }

    public EnergySnapshot createSnapshot() {
        return this.container.createSnapshot();
    }

    public void deserialize(CompoundTag nbt, HolderLookup.Provider provider) {
        this.container.deserialize(nbt, provider);
    }

    public CompoundTag serialize(CompoundTag nbt, HolderLookup.Provider provider) {
        return this.container.serialize(nbt, provider);
    }

    public void update() {
        this.container.serialize(this.stack.getOrCreateTag()); // TODO : find how to fix that
    }

    public void clearContent() {
        this.container.clearContent();
        this.update();
    }

    public ItemStack stack() {
        return this.stack;
    }

    public EnergyContainer container() {
        return this.container;
    }
}
