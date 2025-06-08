package com.st0x0ef.stellaris.common.utils;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.menus.GalaxyMenu;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import com.st0x0ef.stellaris.common.menus.TabletMenu;
import com.st0x0ef.stellaris.common.menus.WaitMenu;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PlanetUtil {

    public static final Component temperature = Component.translatable("text.stellaris.planetscreen.temperature");
    public static final Component gravity = Component.translatable("text.stellaris.planetscreen.gravity");
    public static final Component launch = Component.translatable("text.stellaris.planetscreen.launch");
    public static final Component oxygen = Component.translatable("text.stellaris.planetscreen.oxygen");
    public static final Component system = Component.translatable("text.stellaris.planetscreen.system");
    public static final Component error_message = Component.translatable("text.stellaris.planetscreen.error_message");


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

    public static void ifPlanet(ResourceLocation level, Consumer<Planet> planetRunnable) {
        if(isPlanet(level)) {
            planetRunnable.accept(getPlanet(level));
        }
    }

    public static boolean hasOxygen(Level level) {
        return !isPlanet(level.dimension().location()) || getPlanet(level.dimension().location()).oxygen();
    }

    public static boolean hasOxygenAt(ServerLevel level, @NotNull BlockPos pos) {
        if (!hasOxygen(level)) {
            return GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).hasOxygenAt(pos);
        }

        return true;
    }

    /** Get the resource location of the planet bar set in the Planet file */
    public static ResourceLocation getPlanetBar(ResourceLocation level) {
        if (isPlanet(level)) {
            return getPlanet(level).textures().planet_bar();
        }
        return ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/planet_bar/earth_planet_bar.png");
    }

    public static int openPlanetSelectionMenu(Player player, boolean forceCanGoTo, String galaxyId) {
        Stellaris.LOG.info("Placing space station at " + player.blockPosition());

        ExtendedMenuProvider provider = new ExtendedMenuProvider() {
            @Override
            public void saveExtraData(FriendlyByteBuf buffer) {
                buffer.writeBoolean(forceCanGoTo);
                buffer.writeUtf(galaxyId); // 여기서 galaxyId도 같이 보냄
            }

            @Override
            public Component getDisplayName() {
                return Component.literal("Planets");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                return new PlanetSelectionMenu(syncId, inv, forceCanGoTo, galaxyId);
            }

            public static PlanetSelectionMenu create(int syncId, Inventory inventory, FriendlyByteBuf data) {
                boolean forceCanGoTo = data.readBoolean();
                String galaxyId = data.readUtf();
                return new PlanetSelectionMenu(syncId, inventory, forceCanGoTo, galaxyId);
            }

        };

        if (player instanceof ServerPlayer serverPlayer) {
            MenuRegistry.openExtendedMenu(serverPlayer, provider);
            return 1;
        }

        return 0;
    }


    public static int openWaitMenu(Player player, String playerChoosing) {
        ExtendedMenuProvider provider = new ExtendedMenuProvider() {
            @Override
            public void saveExtraData(FriendlyByteBuf buffer) {
                buffer.writeUtf(playerChoosing);
            }

            @Override
            public @NotNull Component getDisplayName() {
                return Component.literal("Waiting");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
                return WaitMenu.create(syncId, inv, buffer.writeUtf(playerChoosing));
            }
        };

        if (player instanceof ServerPlayer serverPlayer) {
            MenuRegistry.openExtendedMenu(serverPlayer, provider);
            return 1;
        }

        return 0;
    }

    public static int openTabletMenu(Player player, ResourceLocation entry) {
        ExtendedMenuProvider provider = new ExtendedMenuProvider() {
            @Override
            public void saveExtraData(FriendlyByteBuf buffer) {
                buffer.writeResourceLocation(entry);

            }

            @Override
            public Component getDisplayName() {
                return Component.literal("Tablet");
            }

            @Override
            public @NotNull AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
                buffer.writeResourceLocation(entry);

                return TabletMenu.create(syncId, inv, buffer);
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
                return GalaxyMenu.create(syncId, inv, buffer);
            }
        };

        if (player instanceof ServerPlayer serverPlayer) {
            MenuRegistry.openExtendedMenu(serverPlayer, provider);
            return 1;
        }

        return 0;
    }

    public static MutableComponent[] getPlanetInfo(Planet planet) {
        MutableComponent temperatureV = Component.literal(temperature.getString() + " : " + (int) planet.temperature() + "°C");

        MutableComponent oxygenV = Component.literal(oxygen.getString());

        MutableComponent gravityV = Component.literal(gravity.getString() + " : " + String.valueOf(Utils.MCGToMPS2(planet.gravity())).substring(0, 4) + "m/s");

        MutableComponent systemV = Component.literal(system.getString() + " : " + Component.translatable(planet.system()).getString());


        if (planet.oxygen()) {
            oxygenV.withColor(Utils.getColorHexCode("Lime"));
        } else {
            oxygenV.withColor(Utils.getColorHexCode("Red"));
        }

        if (planet.temperature() >= 100) {
            temperatureV.withColor(Utils.getColorHexCode("DarkRed"));

        } else if (planet.temperature() >= 0){
            temperatureV.withColor(Utils.getColorHexCode("Lime"));
        } else if (planet.temperature() >= -100) {
            temperatureV.withColor(Utils.getColorHexCode("Cyan"));
        } else {
            temperatureV.withColor(Utils.getColorHexCode("Blue"));
        }

        return new MutableComponent[] {temperatureV, oxygenV, gravityV, systemV};
    }

    public static MutableComponent getInLinePlanetInfo(Planet planet) {
        MutableComponent[] component = getPlanetInfo(planet);

        return Component.literal("")
                .append(component[0])
                .append(" | ")
                .append(component[1])
                .append(" | ")
                .append(component[2]);
    }


}
