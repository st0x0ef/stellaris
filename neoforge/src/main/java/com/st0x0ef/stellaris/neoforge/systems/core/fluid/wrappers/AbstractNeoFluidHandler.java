package com.st0x0ef.stellaris.neoforge.systems.core.fluid.wrappers;

import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.common.systems.core.storage.util.TransferUtil;
import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.neoforge.systems.core.fluid.util.ConversionUtils;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface AbstractNeoFluidHandler extends IFluidHandler {
    CommonStorage<FluidResource> container();

    @Override
    default int getTanks() {
        return container().size();
    }

    @Override
    default @NotNull FluidStack getFluidInTank(int i) {
        StorageSlot<FluidResource> slot = container().get(i);
        return ConversionUtils.convert(slot.getResource(), slot.getAmount());
    }

    @Override
    default int getTankCapacity(int i) {
        return (int) container().get(i).getLimit(FluidResource.BLANK);
    }

    @Override
    default boolean isFluidValid(int i, FluidStack fluidStack) {
        return container().get(i).isResourceValid(FluidResource.of(fluidStack.getFluid(), fluidStack.getComponentsPatch()));
    }

    @Override
    default int fill(FluidStack fluidStack, FluidAction fluidAction) {
        FluidResource resource = ConversionUtils.convert(fluidStack);
        long amount = container().insert(resource, fluidStack.getAmount(), fluidAction.simulate());
        if (fluidAction.execute()) UpdateManager.batch(container());
        return (int) amount;
    }

    @Override
    default FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        FluidResource resource = ConversionUtils.convert(fluidStack);
        long amount = container().extract(resource, fluidStack.getAmount(), fluidAction.simulate());
        if (fluidAction.execute()) UpdateManager.batch(container());
        return amount > 0 ? ConversionUtils.convert(resource, amount) : FluidStack.EMPTY;
    }

    @Override
    default FluidStack drain(int i, FluidAction fluidAction) {
        Optional<FluidResource> resource = TransferUtil.findResource(container(), (fluidresource) -> !fluidresource.isBlank());
        if (resource.isPresent()) {
            long amount = container().extract(resource.get(), i, fluidAction.simulate());
            UpdateManager.batch(container());
            return amount > 0 ? ConversionUtils.convert(resource.get(), amount) : FluidStack.EMPTY;
        } else {
            return FluidStack.EMPTY;
        }
    }
}
