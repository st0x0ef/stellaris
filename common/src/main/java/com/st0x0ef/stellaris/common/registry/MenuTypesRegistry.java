package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.menus.*;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class MenuTypesRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE = DeferredRegister.create(Stellaris.MODID, Registries.MENU);
    public static final RegistrySupplier<MenuType<RocketStationMenu>> ROCKET_STATION = MENU_TYPE.register("rocket_station", () -> MenuRegistry.ofExtended(RocketStationMenu::new));
    public static final RegistrySupplier<MenuType<RocketMenuMenu>> ROCKET_MENU = MENU_TYPE.register("rocket_menu", () -> MenuRegistry.ofExtended(RocketMenuMenu::new));
    public static final RegistrySupplier<MenuType<SolarPanelMenu>> SOLAR_PANEL_MENU = MENU_TYPE.register("solar_panel", () -> MenuRegistry.ofExtended(SolarPanelMenu::create));
    public static final RegistrySupplier<MenuType<CoalGeneratorMenu>> COAL_GENERATOR_MENU = MENU_TYPE.register("coal_generator", () -> MenuRegistry.ofExtended(CoalGeneratorMenu::create));
    public static final RegistrySupplier<MenuType<RadioactiveGeneratorMenu>> RADIOACTIVE_GENERATOR_MENU = MENU_TYPE.register("radioactive_generator", () -> MenuRegistry.ofExtended(RadioactiveGeneratorMenu::create));
    public static final RegistrySupplier<MenuType<VacumatorMenu>> VACUMATOR_MENU = MENU_TYPE.register("vacumator", () -> MenuRegistry.ofExtended(VacumatorMenu::new));

}
