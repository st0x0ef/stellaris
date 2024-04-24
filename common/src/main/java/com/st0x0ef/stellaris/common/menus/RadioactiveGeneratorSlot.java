package com.st0x0ef.stellaris.common.menus;

import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

//TODO move to radioactive generator menu
public class RadioactiveGeneratorSlot extends Slot {

    public RadioactiveGeneratorSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(TagRegistry.RADIOACTIVE_GENERATOR_FUEL_TAG);
    }
}
