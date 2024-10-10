package com.st0x0ef.stellaris.common.items.module;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public interface SpaceSuitModule {

    MutableComponent displayName(); //TODO add in gui

    default void tick(ItemStack stack, Level level, Player player) {}

    default void addToTooltips(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {}

    @Environment(EnvType.CLIENT)
    default void renderToGui(GuiGraphics graphics, DeltaTracker deltaTracker, Player player, ItemStack stack) {}

    /**
     *
     * @param graphics
     * @param deltaTracker
     * @param player
     * @param stack
     * @param y Y level you use for your first render (increment by texture height/font length
     * @return Y level of you used for your last render (usually var y)
     */
    @Environment(EnvType.CLIENT)
    default int renderStackedGui(GuiGraphics graphics, DeltaTracker deltaTracker, Player player, ItemStack stack, int y) {
        return y;
    }

//    @Environment(EnvType.CLIENT)
//    default ResourceLocation modelTextureOverride() { //TODO layer this texture onto spacesuit texture
//        return null;
//    }

}
