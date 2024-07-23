package com.st0x0ef.stellaris.platform.systems.core.fluid.wrappers;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;

public record NeoFluidContainer(CommonStorage<FluidResource> container) implements AbstractNeoFluidHandler {
}
