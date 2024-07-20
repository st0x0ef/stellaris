package com.st0x0ef.stellaris.neoforge.systems.core.fluid.wrappers;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public record CommonFluidContainer(IFluidHandler handler) implements AbstractCommonFluidContainer {
}
