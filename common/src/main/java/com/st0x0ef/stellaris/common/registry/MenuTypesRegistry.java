package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.menus.RocketMenuMenu;
import com.st0x0ef.stellaris.common.menus.RocketStationMenu;
import com.st0x0ef.stellaris.common.menus.SolarPanelMenu;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class MenuTypesRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE = DeferredRegister.create(Stellaris.MODID, Registries.MENU);
    public static final RegistrySupplier<MenuType<RocketStationMenu>> ROCKET_STATION = MENU_TYPE.register("rocket_station", () -> MenuRegistry.of(RocketStationMenu::new));
    public static final RegistrySupplier<MenuType<RocketMenuMenu>> ROCKET_MENU = MENU_TYPE.register("rocket_menu", () -> MenuRegistry.of(RocketMenuMenu::new));

    public static final RegistrySupplier<MenuType<SolarPanelMenu>> SOLAR_PANEL_MENU = MENU_TYPE.register("solar_panel", () -> MenuRegistry.ofExtended(SolarPanelMenu::create));

}
