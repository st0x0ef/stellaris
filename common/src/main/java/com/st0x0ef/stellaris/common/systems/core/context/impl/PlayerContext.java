package com.st0x0ef.stellaris.common.systems.core.context.impl;

import com.st0x0ef.stellaris.common.systems.core.context.ItemContext;
import com.st0x0ef.stellaris.common.systems.core.impl.vanilla.PlayerContainer;
import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
import com.st0x0ef.stellaris.common.systems.resources.item.ItemResource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record PlayerContext(PlayerContainer outerContainer, StorageSlot<ItemResource> mainSlot) implements ItemContext {
    public static PlayerContext ofHand(Player player, InteractionHand hand) {
        PlayerContainer playerContainer = new PlayerContainer.AutoDrop(player.getInventory());
        return new PlayerContext(playerContainer, playerContainer.getHandSlot(hand));
    }

    public static PlayerContext ofSlot(Player player, int slot) {
        ItemStack stack = player.getInventory().getItem(slot);
        PlayerContainer playerContainer = new PlayerContainer.AutoDrop(player.getInventory());
        return new PlayerContext(playerContainer, playerContainer.get(slot));
    }
}
