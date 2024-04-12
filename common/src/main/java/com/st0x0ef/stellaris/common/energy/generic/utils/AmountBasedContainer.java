package com.st0x0ef.stellaris.common.energy.generic.utils;

import net.minecraft.world.Clearable;

public interface AmountBasedContainer extends Clearable {

    long getStoredAmount();

    long getCapacity();

    boolean allowsInsertion();

    boolean allowsExtraction();

    long insert(long amount, boolean simulate);

    long extract(long amount, boolean simulate);

    long maxInsert();

    long maxExtract();
}
