package com.st0x0ef.stellaris.platform.systems.core.fluid.wrappers;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.platform.systems.core.fluid.util.ConversionUtils;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public interface AbstractCommonFluidContainer extends CommonStorage<FluidResource> {
    IFluidHandler handler();

    @Override
    default int size() {
        return handler().getTanks();
    }

    @Override
    default long insert(FluidResource resource, long amount, boolean simulate) {
        return handler().fill(ConversionUtils.convert(resource, amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    default long extract(FluidResource resource, long amount, boolean simulate) {
        return handler().drain(ConversionUtils.convert(resource, amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE).getAmount();
    }

    @Override
    default @NotNull StorageSlot<FluidResource> get(int index) {
        return new DelegatingFluidHandlerSlot(this, index);
    }
}
