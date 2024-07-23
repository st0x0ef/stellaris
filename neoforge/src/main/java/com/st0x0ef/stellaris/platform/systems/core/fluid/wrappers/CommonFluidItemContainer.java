package com.st0x0ef.stellaris.platform.systems.core.fluid.wrappers;

import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.util.TransferUtil;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.platform.systems.resources.item.ItemResource;
import com.st0x0ef.stellaris.platform.systems.core.fluid.util.ConversionUtils;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public final class CommonFluidItemContainer implements AbstractCommonFluidContainer {
    private final IFluidHandlerItem handler;
    private final ItemContext context;
    private ItemStack lastContainer;

    public CommonFluidItemContainer(IFluidHandlerItem handler, ItemContext context) {
        this.handler = handler;
        this.context = context;
        this.lastContainer = handler.getContainer().copy();
    }

    @Override
    public int size() {
        return handler.getTanks();
    }

    @Override
    public long insert(FluidResource resource, long amount, boolean simulate) {
        int fill = handler.fill(ConversionUtils.convert(resource, amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        if (!simulate) {
            updateContext();
        }
        return fill;
    }

    @Override
    public long extract(FluidResource resource, long amount, boolean simulate) {
        int drain = handler.drain(ConversionUtils.convert(resource, amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE).getAmount();
        if (!simulate) {
            updateContext();
        }
        return drain;
    }

    public void updateContext() {
        if(ItemStack.matches(lastContainer, handler.getContainer())) {
            return;
        }
        lastContainer = handler.getContainer().copy();
        if (context.getResource().test(lastContainer)) {
            TransferUtil.equalize(context.mainSlot(), lastContainer.getCount());
        } else {
            if (context.exchange(ItemResource.of(lastContainer), lastContainer.getCount(), false) != lastContainer.getCount()) {
                context.extract(ItemResource.of(lastContainer), lastContainer.getCount(), false);
                context.mainSlot().insert(ItemResource.of(lastContainer), lastContainer.getCount(), false);
            }
        }
    }

    @Override
    public IFluidHandlerItem handler() {
        return handler;
    }
}