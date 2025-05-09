package com.st0x0ef.stellaris.common.items.armors;

import com.fej1fun.potentials.fluid.UniversalFluidItemStorage;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.SingleFluidStorage;
import dev.architectury.fluid.FluidStack;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceSuitFluidsStorage implements UniversalFluidItemStorage {

    public final ArrayList<SingleFluidStorage> storages;
    protected final ItemStack stack;

    public SpaceSuitFluidsStorage(SingleFluidStorage storage, ItemStack stack) {
        this.storages = new ArrayList<>(List.of(storage));
        this.stack = stack;
    }

    @Override
    public int getTanks() {
        return storages.size();
    }

    public void addStorage(SingleFluidStorage storage) {
        storages.add(storage);
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return storages.get(tank).getFluidInTank(0);
    }

    @Override
    public long getTankCapacity(int tank) {
        return storages.get(tank).getTankCapacity(0);
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return storages.get(tank).isFluidValid(0, stack);
    }

    @Override
    public long fill(FluidStack stack, boolean simulate) {

        for(SingleFluidStorage storage : storages) {
            if (storage.isFluidValid(0, stack)) {
                long filled = storage.fill(stack, simulate);
                if (filled > 0) {
                    return filled;
                }
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack stack, boolean simulate) {
        for(SingleFluidStorage storage : storages) {
            if (storage.isFluidValid(0, stack)) {
                FluidStack drained = storage.drain(stack, simulate);
                if (!drained.isEmpty()) {
                    return drained;
                }
            }
        }
        return FluidStack.empty();
    }

    @Override
    public @NotNull Iterator<FluidStack> iterator() {
        List<FluidStack> iterators = new ArrayList<>();
        for (SingleFluidStorage storage : storages) {
            iterators.add(storage.getFluidInTank(0));
        }
        return iterators.iterator();
    }



    @Override
    public ItemStack getContainer() {
        return this.stack;
    }
}
