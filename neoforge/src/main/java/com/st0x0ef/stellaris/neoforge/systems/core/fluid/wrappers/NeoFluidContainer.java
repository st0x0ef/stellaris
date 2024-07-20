package com.st0x0ef.stellaris.neoforge.systems.core.fluid.wrappers;

import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;

public record NeoFluidContainer(CommonStorage<FluidResource> container) implements AbstractNeoFluidHandler {
}
