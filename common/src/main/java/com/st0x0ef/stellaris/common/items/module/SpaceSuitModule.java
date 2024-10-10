package com.st0x0ef.stellaris.common.items.module;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public interface SpaceSuitModule {

    Component displayName(); //TODO add in gui

    default void tick(ItemStack stack, Level level, Player player) {}

    default void addToTooltips(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

    }

    @Environment(EnvType.CLIENT)
    default int renderToGui(GuiGraphics graphics, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int layer) {
        return 0;
    } //TODO add in gui

//    @Environment(EnvType.CLIENT)
//    default ResourceLocation modelTextureOverride() { //TODO layer this texture onto spacesuit texture
//        return null;
//    }

}
