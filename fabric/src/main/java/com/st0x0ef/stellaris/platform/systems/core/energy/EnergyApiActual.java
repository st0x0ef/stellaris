package com.st0x0ef.stellaris.platform.systems.core.energy;

import com.st0x0ef.stellaris.platform.systems.core.CommonStorageLib;
import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.ValueStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.platform.systems.core.energy.lookup.EnergyBlockLookup;
import com.st0x0ef.stellaris.platform.systems.core.energy.lookup.EnergyItemLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

@Actual
public class EnergyApiActual {
    @Actual
    public static final BlockLookup<ValueStorage, Direction> BLOCK = new EnergyBlockLookup();
    @Actual
    public static final ItemLookup<ValueStorage, ItemContext> ITEM = new EnergyItemLookup();
    @Actual
    public static final EntityLookup<ValueStorage, Direction> ENTITY = EntityLookup.create(new ResourceLocation(CommonStorageLib.MOD_ID, "entity_energy"), ValueStorage.class, Direction.class);
}
