package com.st0x0ef.stellaris.common.items.module;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface SpaceSuitModule {

    default void tick(ItemStack stack, Level level, Player player) {}

    Component displayName(); //TODO add in gui

    default ResourceLocation modelTextureOverride() { //TODO layer this texture onto spacesuit texture
        return null;
    }

}
