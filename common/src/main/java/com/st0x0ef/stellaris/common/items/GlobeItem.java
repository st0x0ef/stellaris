package com.st0x0ef.stellaris.common.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class GlobeItem extends BlockItem implements CustomTabletEntry {

    public GlobeItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ResourceLocation getEntryName(ItemStack stack) {
        return ResourceLocation.fromNamespaceAndPath("planets", this.arch$registryName().getPath().replace("_globe", ""));
    }


}
