package com.st0x0ef.stellaris.common.systems.fluid.impl;

import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;
import net.minecraft.util.Mth;

import java.util.function.BiPredicate;
import java.util.function.IntToLongFunction;

public class InsertOnlyFluidContainer extends SimpleFluidContainer {
    public InsertOnlyFluidContainer(IntToLongFunction maxAmount, int tanks, BiPredicate<Integer, FluidHolder> fluidFilter) {
        super(maxAmount, tanks, fluidFilter);
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        return FluidHolder.empty();
    }

    @Override
    public FluidHolder internalExtract(FluidHolder fluid, boolean simulate) {
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
    public boolean allowsExtraction() {
        return false;
    }
}
