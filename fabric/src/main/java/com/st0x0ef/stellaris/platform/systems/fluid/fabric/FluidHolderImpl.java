package com.st0x0ef.stellaris.platform.systems.fluid.fabric;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.fabric.fluid.holder.FabricFluidHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(FluidHolder.class)
public interface FluidHolderImpl {
    @ImplementsBaseElement
    static FluidHolder of(Fluid fluid, long amount, CompoundTag tag) {
        return FabricFluidHolder.of(fluid, amount, tag);
    }

    @ImplementsBaseElement
    static FluidHolder empty() {
        return FabricFluidHolder.empty();
    }
}
