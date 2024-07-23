package com.st0x0ef.stellaris.platform.systems.core.fluid;

import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

@Expect
public class FluidApi {
    public static final BlockLookup<CommonStorage<FluidResource>, @Nullable Direction> BLOCK;
    public static final ItemLookup<CommonStorage<FluidResource>, ItemContext> ITEM;
    public static final EntityLookup<CommonStorage<FluidResource>, Direction> ENTITY;
}
