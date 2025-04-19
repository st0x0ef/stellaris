package com.st0x0ef.stellaris.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class CableItem extends BlockItem implements CustomTabletEntry {

    private final int energy;

    public CableItem(Block block, Properties properties, int energy) {
        super(block, properties);
        this.energy = energy;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        consumer.accept(Component.literal(energy + " FE/t").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public ResourceLocation getEntryName(ItemStack stack) {
        return ResourceLocation.parse("items:cable");
    }
}
