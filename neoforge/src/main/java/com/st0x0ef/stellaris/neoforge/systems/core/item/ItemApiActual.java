package com.st0x0ef.stellaris.neoforge.systems.core.item;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.common.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import com.st0x0ef.stellaris.neoforge.systems.core.item.lookup.ItemBlockLookup;
import com.st0x0ef.stellaris.neoforge.systems.core.item.lookup.ItemEntityLookup;
import com.st0x0ef.stellaris.neoforge.systems.core.item.lookup.ItemItemLookup;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class ItemApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<ItemResource>, @Nullable Direction> BLOCK = ItemBlockLookup.INSTANCE;
    @Actual
    public static final ItemLookup<CommonStorage<ItemResource>, ItemContext> ITEM = ItemItemLookup.INSTANCE;
    @Actual
    public static final EntityLookup<CommonStorage<ItemResource>, Void> ENTITY = ItemEntityLookup.INSTANCE;
    @Actual
    public static final EntityLookup<CommonStorage<ItemResource>, Direction> ENTITY_AUTOMATION = ItemEntityLookup.AUTOMATION;
}
