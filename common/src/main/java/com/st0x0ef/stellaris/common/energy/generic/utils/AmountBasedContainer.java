package com.st0x0ef.stellaris.common.energy.generic.utils;

import net.minecraft.world.Clearable;

public interface AmountBasedContainer extends Clearable {
    long getStoredAmount();

    long getCapacity();

    boolean allowsInsertion();

    boolean allowsExtraction();

    long insert(long var1, boolean var3);

    long extract(long var1, boolean var3);

    long maxInsert();

    long maxExtract();
}
