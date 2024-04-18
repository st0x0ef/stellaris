package com.st0x0ef.stellaris.common.energy.generic.utils;

import net.minecraft.world.Clearable;
import org.jetbrains.annotations.NotNull;

public interface StackBasedContainer<T> extends Clearable {

    int getSlotCount();

    @NotNull
    T getValueInSlot(int slot);

    int getSlotLimit(int slot);

    boolean isValueValid(int slot, @NotNull T value);

    @NotNull
    T insert(@NotNull T value, boolean simulate);

    @NotNull
    T extract(int amount, boolean simulate);

    @NotNull
    T extractFromSlot(int slot, int amount, boolean simulate);
}
