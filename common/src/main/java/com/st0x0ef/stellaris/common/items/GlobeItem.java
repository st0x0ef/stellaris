package com.st0x0ef.stellaris.common.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class GlobeItem extends BlockItem {

    private final ResourceLocation texture;

    public GlobeItem(Block block, ResourceLocation texture, Properties properties) {
        super(block, properties);
        this.texture = texture;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

}
