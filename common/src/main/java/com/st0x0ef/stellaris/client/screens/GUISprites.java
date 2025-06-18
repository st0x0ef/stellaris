package com.st0x0ef.stellaris.client.screens;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import net.minecraft.resources.ResourceLocation;

import static com.st0x0ef.stellaris.Stellaris.id;
import static com.st0x0ef.stellaris.Stellaris.texture;

public class GUISprites {

    public static final ResourceLocation WATER_OVERLAY = ResourceLocationUtils.id("util/water_overlay");
    public static final ResourceLocation WATER_SEPARATOR_OVERLAY = ResourceLocationUtils.id("util/water_separator_overlay");
    public static final ResourceLocation HYDROGEN_OVERLAY = ResourceLocationUtils.id("util/hydrogen_gui_overlay");
    public static final ResourceLocation OXYGEN_OVERLAY = ResourceLocationUtils.id("util/oxygen_gui_overlay");
    public static final ResourceLocation SIDEWAYS_ENERGY_FULL = ResourceLocationUtils.id("util/sideway_energy_full");
    public static final ResourceLocation DIESEL_OVERLAY = ResourceLocationUtils.id("util/diesel_overlay");
    public static final ResourceLocation SIDEWAYS_BATTERY_OVERLAY = ResourceLocationUtils.id("util/sideway_battery_overlay");
    public static final ResourceLocation SIDEWAYS_BATTERY_OVERLAY_OLD = ResourceLocationUtils.id("util/sideway_battery_overlay_old");
    public static final ResourceLocation LIQUID_TANK_OVERLAY = ResourceLocationUtils.id("util/water_tank_overlay");
    public static final ResourceLocation OIL_OVERLAY = ResourceLocationUtils.id("util/oil_gui_overlay");
    public static final ResourceLocation FUEL_OVERLAY = ResourceLocationUtils.id("util/fuel_overlay");
    public static final ResourceLocation BATTERY_OVERLAY = ResourceLocationUtils.id("util/battery_overlay");
    public static final ResourceLocation BATTERY_OVERLAY_OLD = ResourceLocationUtils.id("util/battery_overlay_old");
    public static final ResourceLocation ENERGY_FULL = ResourceLocationUtils.id("util/energy_full");
    public static final ResourceLocation COAL_GENERATOR_LIT_PROGRESS_SPRITE = ResourceLocationUtils.id("util/coal_generator_fire_full");
    public static final ResourceLocation RADIOACTIVE_GENERATOR_LIT_PROGRESS_SPRITE = ResourceLocationUtils.id("util/radioactive_generator_fire_full");
    public static final ResourceLocation FLUID_TANK_OVERLAY = ResourceLocationUtils.id("util/fluid_tank_overlay");
    public static final ResourceLocation NO_OVERLAY = ResourceLocationUtils.id("util/no_overlay");
    public static final ResourceLocation SPACESUIT_OXYGEN_BAR = ResourceLocationUtils.texture("overlay/oxygen_hud");
    public static final ResourceLocation SPACESUIT_FUEL_BAR = ResourceLocationUtils.texture("overlay/fuel_hud");
    public static final ResourceLocation SPACESUIT_ENERGY_BAR = ResourceLocationUtils.texture("overlay/energy_hud");
    public static final ResourceLocation SPACESUIT_FULL_BAR_SPRITE =ResourceLocationUtils.id("util/bar_full");

    public static final ResourceLocation INDUSTRIAL_CHECKBOX = ResourceLocationUtils.id("util/industrial_checkbox");
    public static final ResourceLocation INDUSTRIAL_CHECKBOX_SELECTED = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "util/industrial_checkbox_selected");
    public static final ResourceLocation CHECKBOX = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "util/checkbox");
    public static final ResourceLocation CHECKBOX_SELECTED = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "util/checkbox_selected");
    public static final ResourceLocation EDIT_BAR = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "util/edit_bar");

    public static final ResourceLocation WINDOW_BAR = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "util/window_bar");
    public static final ResourceLocation FLAMES = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "util/flames");
}
