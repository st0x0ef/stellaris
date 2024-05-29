package com.st0x0ef.stellaris.common.energy.generic.utils;

import net.minecraft.world.Clearable;
import org.jetbrains.annotations.NotNull;

public interface StackBasedContainer<T> extends Clearable {
    int getSlotCount();

    @NotNull T getValueInSlot(int var1);

    int getSlotLimit(int var1);

    boolean isValueValid(int var1, @NotNull T var2);

    @NotNull T insert(@NotNull T var1, boolean var2);

    @NotNull T extract(int var1, boolean var2);

    @NotNull T extractFromSlot(int var1, int var2, boolean var3);
}
