package com.st0x0ef.stellaris.neoforge.systems.core.fluid.wrappers;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public record NeoFluidItemContainer(CommonStorage<FluidResource> container, ItemContext context) implements AbstractNeoFluidHandler, IFluidHandlerItem {
    @Override
    public @NotNull ItemStack getContainer() {
        return context.getResource().toStack((int) context.getAmount());
    }
}
