package com.st0x0ef.stellaris.common.systems.core.item;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.common.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("NoMatchingActual")
@Expect
public class ItemApi {
    public static final BlockLookup<CommonStorage<ItemResource>, @Nullable Direction> BLOCK;
    public static final ItemLookup<CommonStorage<ItemResource>, ItemContext> ITEM;
    public static final EntityLookup<CommonStorage<ItemResource>, Void> ENTITY;
    public static final EntityLookup<CommonStorage<ItemResource>, Direction> ENTITY_AUTOMATION;
}
