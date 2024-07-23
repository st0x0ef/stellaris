package com.st0x0ef.stellaris.platform.systems.core.item;

import com.st0x0ef.stellaris.platform.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.platform.systems.resources.item.ItemResource;
import com.st0x0ef.stellaris.platform.systems.core.item.lookup.ItemBlockLookup;
import com.st0x0ef.stellaris.platform.systems.core.item.lookup.ItemEntityLookup;
import com.st0x0ef.stellaris.platform.systems.core.item.lookup.ItemItemLookup;
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
