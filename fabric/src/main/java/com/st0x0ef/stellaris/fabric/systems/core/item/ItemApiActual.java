package com.st0x0ef.stellaris.fabric.systems.core.item;

import com.st0x0ef.stellaris.common.systems.core.CommonStorageLib;
import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.common.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.common.systems.lookup.ItemLookup;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import com.st0x0ef.stellaris.fabric.systems.core.wrapped.WrappedBlockLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class ItemApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<ItemResource>, @Nullable Direction> BLOCK = new WrappedBlockLookup.ofItem();
    @Actual
    public static final ItemLookup<CommonStorage<ItemResource>, ItemContext> ITEM = ItemLookup.create(new ResourceLocation(CommonStorageLib.MOD_ID, "item_item"), CommonStorage.asClass(), ItemContext.class);
    @Actual
    public static final EntityLookup<CommonStorage<ItemResource>, Void> ENTITY = EntityLookup.create(new ResourceLocation(CommonStorageLib.MOD_ID, "entity_item"), CommonStorage.asClass(), Void.class);
    @Actual
    public static final EntityLookup<CommonStorage<ItemResource>, Direction> ENTITY_AUTOMATION = EntityLookup.create(new ResourceLocation(CommonStorageLib.MOD_ID, "entity_item_automation"), CommonStorage.asClass(), Direction.class);
}
