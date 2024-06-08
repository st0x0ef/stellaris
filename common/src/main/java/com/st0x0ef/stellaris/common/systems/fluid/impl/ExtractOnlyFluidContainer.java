package com.st0x0ef.stellaris.common.systems.fluid.impl;

import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;
import net.minecraft.util.Mth;

import java.util.function.BiPredicate;
import java.util.function.IntToLongFunction;

public class ExtractOnlyFluidContainer extends SimpleFluidContainer {
    public ExtractOnlyFluidContainer(IntToLongFunction maxAmount, int tanks, BiPredicate<Integer, FluidHolder> fluidFilter) {
        super(maxAmount, tanks, fluidFilter);
    }

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        return 0;
    }

    @Override
    public long internalInsert(FluidHolder fluid, boolean simulate) {
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
    public boolean allowsInsertion() {
        return false;
    }
}
