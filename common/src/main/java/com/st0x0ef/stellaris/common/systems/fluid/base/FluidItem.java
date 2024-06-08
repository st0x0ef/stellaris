package com.st0x0ef.stellaris.common.systems.fluid.base;

import com.st0x0ef.stellaris.common.systems.fluid.impl.WrappedItemFluidContainer;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import net.minecraft.world.item.ItemStack;

/**
 * An interface that represents an item that can provide a fluid container for a Botarium block.
 *
 * @param <T> The type of the fluid container. Must implement the {@link ItemFluidContainer} and {@link Updatable} interfaces. Botarium provides a default implementation for this with {@link WrappedItemFluidContainer}.
 */
public interface FluidItem<T extends ItemFluidContainer & Updatable> {

    /**
     * @return The {@link ItemFluidContainer} for the block.
     */
    T getFluidContainer(ItemStack holder);
}