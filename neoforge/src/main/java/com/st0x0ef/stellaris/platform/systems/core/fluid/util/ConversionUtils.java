package com.st0x0ef.stellaris.platform.systems.core.fluid.util;

import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;
import net.neoforged.neoforge.fluids.FluidStack;

public class ConversionUtils {
    public static FluidStack convert(FluidResource resource, long amount) {
        FluidStack stack = new FluidStack(resource.getType(), (int) amount);
        stack.applyComponents(resource.getDataPatch());
        return stack;
    }

    public static FluidResource convert(FluidStack stack) {
        return FluidResource.of(stack.getFluid(), stack.getComponentsPatch());
    }
}
