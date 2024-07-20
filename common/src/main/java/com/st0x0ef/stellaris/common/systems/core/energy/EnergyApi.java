package com.st0x0ef.stellaris.common.systems.core.energy;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.ValueStorage;
import com.st0x0ef.stellaris.common.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.common.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;

@Expect
public class EnergyApi {
    public static final BlockLookup<ValueStorage, Direction> BLOCK;
    public static final ItemLookup<ValueStorage, ItemContext> ITEM;
    public static final EntityLookup<ValueStorage, Direction> ENTITY;
}
