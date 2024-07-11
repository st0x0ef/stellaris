package com.st0x0ef.stellaris.common.utils;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.menus.MilkyWayMenu;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PlanetUtil {
    public static Planet getPlanet(ResourceLocation level) {
        AtomicReference<Planet> p = new AtomicReference<>();
        StellarisData.getPlanets().forEach(planet -> {if (planet.dimension().equals(level)) p.set(planet);});
        return p.get();
    }

    public static boolean isPlanet(ResourceLocation level) {
        AtomicBoolean isPlanet = new AtomicBoolean(false);
        StellarisData.getPlanets().forEach(planet -> {if (planet.dimension().equals(level)) isPlanet.set(true);});
        return isPlanet.get();
    }
    public static boolean hasOxygen(ResourceLocation level) {
        if (isPlanet(level)) {
            return getPlanet(level).oxygen();
        }
        return true;
    }

    /** Get the resourcelocation of the planet bar set in the Planet file */
    public static ResourceLocation getPlanetBar(ResourceLocation level) {
        if (isPlanet(level)) {
            return getPlanet(level).textures().planet_bar();
        }
        return new ResourceLocation("stellaris", "textures/planet_bar/earth_planet_bar.png");
    }

    public static int openPlanetSelectionMenu(Player player, boolean forceCanGoTo) {
        ExtendedMenuProvider provider = new ExtendedMenuProvider() {
            @Override
            public void saveExtraData(FriendlyByteBuf buffer) {
                buffer.writeBoolean(forceCanGoTo);
            }

            @Override
            public Component getDisplayName() {
                return Component.literal("Planets");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
                return PlanetSelectionMenu.create(syncId, inv, buffer.writeBoolean(forceCanGoTo));
            }
        };

        if (player instanceof ServerPlayer serverPlayer) {
            MenuRegistry.openExtendedMenu(serverPlayer, provider);
            return 1;
        }

        return 0;
    }

    public static int openMilkyWayMenu(Player player) {
        ExtendedMenuProvider provider = new ExtendedMenuProvider() {

            @Override
            public void saveExtraData(FriendlyByteBuf buf) {

            }

            @Override
            public Component getDisplayName() {
                return Component.literal("MilkyWay");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
                return MilkyWayMenu.create(syncId, inv, buffer);
            }
        };

        if (player instanceof ServerPlayer serverPlayer) {
            MenuRegistry.openExtendedMenu(serverPlayer, provider);
            return 1;
        }

        return 0;
    }
}
