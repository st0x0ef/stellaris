package com.st0x0ef.stellaris.platform.systems.core.energy;

import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.ValueStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.ItemLookup;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;

@Expect
public class EnergyApi {
    @Expect
    public static final BlockLookup<ValueStorage, Direction> BLOCK;
    @Expect
    public static final ItemLookup<ValueStorage, ItemContext> ITEM;
    @Expect
    public static final EntityLookup<ValueStorage, Direction> ENTITY;
}
