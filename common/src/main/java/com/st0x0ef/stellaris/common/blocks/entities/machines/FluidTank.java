package com.st0x0ef.stellaris.common.blocks.entities.machines;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;

public class FluidTank {

    private final String name;
    private final long maxCapacity;
    private FluidStack stack = FluidStack.empty();

    public FluidTank(String name, long maxCapacity) {
        this.name = name;
        this.maxCapacity = (maxCapacity * FluidTankHelper.BUCKET_AMOUNT) + 1;
    }

    public long getMaxCapacity() {
        return maxCapacity;
    }

    public void setFluid(Fluid fluid, long amount) {
        stack = FluidStack.create(fluid, amount);
    }

    public long getAmount() {
        return stack.getAmount();
    }

    public void setAmount(long amount) {
        stack.setAmount(Mth.clamp(amount, 0, maxCapacity));
    }

    public void grow(long amount) {
        setAmount(getAmount() + amount);
    }

    public void shrink(long amount) {
        grow(-amount);
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public FluidStack getStack() {
        return stack;
    }

    public void load(HolderLookup.Provider provider, CompoundTag tag) {
        CompoundTag containerTag = tag.getCompound(name);
        stack = FluidStack.read(provider, containerTag).orElse(FluidStack.empty());
    }

    public void save(HolderLookup.Provider provider, CompoundTag tag) {
        if (!isEmpty()) {
            tag.put(name, stack.write(provider, new CompoundTag()));
        }
    }
}
