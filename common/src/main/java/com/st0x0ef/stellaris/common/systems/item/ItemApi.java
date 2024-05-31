package com.st0x0ef.stellaris.common.systems.item;

import com.st0x0ef.stellaris.common.systems.generic.base.BlockContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.EntityContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.ItemContainerLookup;
import com.st0x0ef.stellaris.platform.systems.item.base.ItemContainer;
import net.minecraft.core.Direction;
import org.apache.commons.lang3.NotImplementedException;

public class ItemApi {
    public static final ItemContainerLookup<ItemContainer, Void> ITEM = getItemLookup();
    public static final BlockContainerLookup<ItemContainer, Direction> SIDED = getBlockLookup();
    public static final EntityContainerLookup<ItemContainer, Void> ENTITY = getEntityLookup();
    public static final EntityContainerLookup<ItemContainer, Direction> ENTITY_AUTOMATION = getEntityAutomationLookup();

    private static ItemContainerLookup<ItemContainer, Void> getItemLookup() {
        throw new NotImplementedException();
    }

    private static BlockContainerLookup<ItemContainer, Direction> getBlockLookup() {
        throw new NotImplementedException();
    }

    private static EntityContainerLookup<ItemContainer, Void> getEntityLookup() {
        throw new NotImplementedException();
    }

    private static EntityContainerLookup<ItemContainer, Direction> getEntityAutomationLookup() {
        throw new NotImplementedException();
    }
}
