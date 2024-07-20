package com.st0x0ef.stellaris.neoforge.systems.core.fluid;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.common.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.neoforge.systems.core.fluid.lookup.FluidBlockLookup;
import com.st0x0ef.stellaris.neoforge.systems.core.fluid.lookup.FluidEntityLookup;
import com.st0x0ef.stellaris.neoforge.systems.core.fluid.lookup.FluidItemLookup;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class FluidApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<FluidResource>, @Nullable Direction> BLOCK = FluidBlockLookup.INSTANCE;
    @Actual
    public static final ItemLookup<CommonStorage<FluidResource>, ItemContext> ITEM = FluidItemLookup.INSTANCE;
    @Actual
    public static final EntityLookup<CommonStorage<FluidResource>, Direction> ENTITY = FluidEntityLookup.INSTANCE;
}
