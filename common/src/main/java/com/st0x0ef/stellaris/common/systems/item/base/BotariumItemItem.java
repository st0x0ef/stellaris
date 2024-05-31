package com.st0x0ef.stellaris.common.systems.item.base;

import com.st0x0ef.stellaris.common.systems.util.Snapshotable;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import com.st0x0ef.stellaris.platform.systems.item.base.ItemContainer;
import net.minecraft.world.item.ItemStack;

public interface BotariumItemItem<T extends ItemContainer & Updatable & Snapshotable<ItemSnapshot>> {
    T getItemContainer(ItemStack stack);
}
