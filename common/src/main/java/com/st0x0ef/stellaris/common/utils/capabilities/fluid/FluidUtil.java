package com.st0x0ef.stellaris.common.utils.capabilities.fluid;

import com.fej1fun.potentials.capabilities.Capabilities;
import com.fej1fun.potentials.fluid.BaseFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidItemStorage;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import dev.architectury.fluid.FluidStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class FluidUtil {
    public static void moveFluidToItem(int tank, UniversalFluidStorage from, int slot, NonNullList<ItemStack> items, long amount) {
        if (items.get(slot).isEmpty()) return;
        UniversalFluidItemStorage to = Capabilities.Fluid.ITEM.getCapability(items.get(slot));
        if (to == null) return;
        amount = Math.min(amount, to.getTankCapacity(0) - to.getFluidInTank(0).getAmount());
        moveFluid(from, to, from.getFluidInTank(tank).copyWithAmount(amount));
        items.set(slot, to.getContainer());
    }

    public static void moveFluidFromItem(int tank, int slot, NonNullList<ItemStack> items, UniversalFluidStorage to, long amount) {
        if (items.get(slot).isEmpty()) return;
        UniversalFluidItemStorage from = Capabilities.Fluid.ITEM.getCapability(items.get(slot));
        if (from == null) return;
        amount = Math.min(amount, to.getTankCapacity(tank) - to.getFluidInTank(tank).getAmount());
        moveFluid(from, to, from.getFluidInTank(tank).copyWithAmount(amount));
        items.set(slot, from.getContainer());
    }

    public static FluidStack moveFluid(UniversalFluidStorage from, UniversalFluidStorage to, FluidStack stack) {
        if (stack.isEmpty()) return FluidStack.empty();

        FluidStack inserted = FluidStack.create(stack, to.fill(from.drain(stack, true), true));

        if (inserted.isEmpty()) return FluidStack.empty();

        from.drain(inserted.copy(), false);
        to.fill(inserted.copy(), false);

        return inserted;
    }

    /// ignores max fill/drain limits
    public static FluidStack moveFluidWithSet(BaseFluidStorage from, BaseFluidStorage to, FluidStack stack) {
        FluidStack inserted = FluidStack.create(stack ,to.fill(from.drain(stack, true), true));

        if (inserted.isEmpty()) return FluidStack.empty();

        from.drainWithoutLimits(inserted, false);
        to.fillWithoutLimits(inserted, false);
        return inserted;
    }
}
