package com.st0x0ef.stellaris.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class CableItem extends BlockItem {
    private final int energy;

    public CableItem(Block block, Properties properties, int energy) {
        super(block, properties);
        this.energy = energy;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal(energy + " FE/t").withStyle(ChatFormatting.GRAY));

    }
}
