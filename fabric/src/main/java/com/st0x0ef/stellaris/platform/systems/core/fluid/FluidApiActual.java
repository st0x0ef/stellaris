package com.st0x0ef.stellaris.platform.systems.core.fluid;

import com.st0x0ef.stellaris.platform.systems.core.CommonStorageLib;
import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.platform.systems.core.wrapped.WrappedBlockLookup;
import com.st0x0ef.stellaris.platform.systems.core.wrapped.WrappedItemLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class FluidApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<FluidResource>, @Nullable Direction> BLOCK = new WrappedBlockLookup.ofFluid();
    @Actual
    public static final ItemLookup<CommonStorage<FluidResource>, ItemContext> ITEM = new WrappedItemLookup.OfFluid();
    @Actual
    public static final EntityLookup<CommonStorage<FluidResource>, Direction> ENTITY = EntityLookup.createAutomation(new ResourceLocation(CommonStorageLib.MOD_ID, "entity_fluid"), CommonStorage.asClass());
}
