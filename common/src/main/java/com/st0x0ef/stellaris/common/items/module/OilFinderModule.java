package com.st0x0ef.stellaris.common.items.module;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class OilFinderModule extends Item implements SpaceSuitModule {

    int oilLevel = 0;

    public OilFinderModule(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void tick(ItemStack stack, Level level, Player player) {
        SpaceSuitModule.super.tick(stack, level, player);
        if(!level.isClientSide) {
            this.oilLevel = level.getChunk(player.blockPosition()).stellaris$getChunkOilLevel();
        }
    }

    @Override
    public void renderToGui(GuiGraphics graphics, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int layer) {
        int th = Minecraft.getInstance().font.lineHeight;
        graphics.drawString(Minecraft.getInstance().font, "Oil Level: " + oilLevel, 5, (layer) * 10  + th, 0xFFFFFF);
    }

    @Override
    public Component displayName() {
        return Component.translatable("spacesuit.stellaris.oil_finder");
    }

    @Override
    public void addToTooltips(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("spacesuit.stellaris.oil_finder.tooltip"));
    }
}
