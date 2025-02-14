package com.st0x0ef.stellaris.common.utils.capabilities.fluid;

import com.fej1fun.potentials.capabilities.Capabilities;
import com.fej1fun.potentials.fluid.BaseFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import dev.architectury.fluid.FluidStack;
import net.minecraft.world.item.ItemStack;

public class FluidUtil {
    public static void moveFluidToItem(int tank, UniversalFluidStorage from, ItemStack stackTo, long amount) {
        if (stackTo.isEmpty()) return;
        UniversalFluidStorage to = Capabilities.Fluid.ITEM.getCapability(stackTo);
        if (to == null) return;
        amount = Math.min(amount, to.getTankCapacity(0) - to.getFluidInTank(0).getAmount());
        moveFluid(from, to, from.getFluidInTank(tank).copyWithAmount(amount));
    }

    public static boolean moveFluidFromItem(int tank, ItemStack stackFrom, UniversalFluidStorage to, long amount) {
        if (stackFrom.isEmpty()) return false;
        UniversalFluidStorage from = Capabilities.Fluid.ITEM.getCapability(stackFrom);
        if (from == null) return false;
        amount = Math.min(amount, to.getTankCapacity(0));
        return !moveFluid(from, to, from.getFluidInTank(tank).copyWithAmount(amount)).isEmpty();
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
