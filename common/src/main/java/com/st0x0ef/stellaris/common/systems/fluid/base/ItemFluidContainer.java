package com.st0x0ef.stellaris.common.systems.fluid.base;

import net.minecraft.world.item.ItemStack;

public interface ItemFluidContainer extends FluidContainer {

    /**
     * @return The {@link ItemStack} that is used as container item for this fluid container.
     */
    ItemStack getContainerItem();
}
