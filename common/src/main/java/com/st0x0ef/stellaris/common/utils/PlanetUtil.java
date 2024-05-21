package com.st0x0ef.stellaris.common.utils;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PlanetUtil {
    public static Planet getPlanet(ResourceKey<Level> level) {
        return StellarisData.PLANETS.get(level);
    }

    public static boolean isPlanet(ResourceKey<Level> level) {
        return StellarisData.PLANETS.containsKey(level);
    }
    public static boolean hasOxygen(ResourceKey<Level> level) {
        if (isPlanet(level)) {
            return getPlanet(level).oxygen();
        }
        return true;
    }

    public static boolean isOrbit(ResourceKey<Level> level) {
        if (isPlanet(level)) {
            return getPlanet(level).oxygen();
        }
        return true;
    }

    public static ResourceLocation getPlanetBar(ResourceKey<Level> level) {
        if (isPlanet(level)) {
            return getPlanet(level).textures().planet_bar();
        }
        return new ResourceLocation("stellaris", "textures/planet_bar/earth_planet_bar.png");
    }

    public static int openPlanetSelectionMenu(Player player) {
        ExtendedMenuProvider provider = new ExtendedMenuProvider() {

            @Override
            public void saveExtraData(FriendlyByteBuf buf) {

            }

            @Override
            public Component getDisplayName() {
                return Component.literal("Planets");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
                return PlanetSelectionMenu.create(syncId, inv, buffer);
            }
        };

        if (player != null && player instanceof ServerPlayer) {
            MenuRegistry.openExtendedMenu((ServerPlayer)player, provider);
            return 1;
        }

        return 0;
    }
}
