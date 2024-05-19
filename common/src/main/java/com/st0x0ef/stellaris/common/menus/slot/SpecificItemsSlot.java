package com.st0x0ef.stellaris.common.menus.slot;

import com.st0x0ef.stellaris.common.registry.TagRegistry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SpecificItemsSlot {

    public static class Tags extends Slot {
        final TagKey<net.minecraft.world.item.Item> tag;
        public Tags(Container container, int slot, int x, int y, TagKey<net.minecraft.world.item.Item> tag) {
            super(container, slot, x, y);
            this.tag = tag;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {

            return stack.is(tag);
        }
    }

    public static class Item extends Slot {
        net.minecraft.world.item.Item item;
        public Item(Container container, int slot, int x, int y, net.minecraft.world.item.Item item) {
            super(container, slot, x, y);
            this.item = item;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(item);
        }
    }

    public static class InstanceOfItem extends Slot {
        Class item;
        public InstanceOfItem(Container container, int slot, int x, int y, Class item) {
            super(container, slot, x, y);
            this.item = item;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem().getClass().isInstance(item);
        }
    }
}