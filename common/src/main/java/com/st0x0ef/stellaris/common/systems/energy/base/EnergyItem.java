package com.st0x0ef.stellaris.common.systems.energy.base;

import com.st0x0ef.stellaris.common.systems.energy.impl.WrappedItemEnergyContainer;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import net.minecraft.world.item.ItemStack;

/**
 * Functional interface that represents an item that can provide an energy storage container.
 *
 * @param <T> The type of energy storage container. Botarium provides a default implementation for this with {@link WrappedItemEnergyContainer}.
 */
@FunctionalInterface
public interface EnergyItem<T extends EnergyContainer & Updatable> {
    /**
     * Retrieves the energy storage container for the given holder.
     *
     * @param holder The holder item stack.
     * @return The energy storage container.
     */
    T getEnergyStorage(ItemStack holder);
}
