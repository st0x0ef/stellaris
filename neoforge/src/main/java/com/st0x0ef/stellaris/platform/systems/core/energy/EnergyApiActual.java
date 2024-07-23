package com.st0x0ef.stellaris.platform.systems.core.energy;

import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.ValueStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.platform.systems.core.energy.lookup.EnergyBlockLookup;
import com.st0x0ef.stellaris.platform.systems.core.energy.lookup.EnergyEntityLookup;
import com.st0x0ef.stellaris.platform.systems.core.energy.lookup.EnergyItemLookup;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;

@Actual
public class EnergyApiActual {
    @Actual
    public static final BlockLookup<ValueStorage, Direction> BLOCK = EnergyBlockLookup.INSTANCE;
    @Actual
    public static final ItemLookup<ValueStorage, ItemContext> ITEM = EnergyItemLookup.INSTANCE;
    @Actual
    public static final EntityLookup<ValueStorage, Direction> ENTITY = EnergyEntityLookup.INSTANCE;
}
