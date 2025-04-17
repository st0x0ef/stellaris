package com.st0x0ef.stellaris.common.utils;

import net.minecraft.world.entity.player.Player;

public interface CustomPlayerData {

    default boolean stellaris$isPlanetMenuOpen() {
        return false;
    }

    void stellaris$setPlanetMenuOpen(boolean open, Player player, boolean sync);
}
