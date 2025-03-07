package com.st0x0ef.stellaris.common.compats.rei;

import com.st0x0ef.stellaris.common.data.recipes.FuelRefineryRecipe;
import com.st0x0ef.stellaris.common.data.recipes.RocketStationRecipe;
import com.st0x0ef.stellaris.common.data.recipes.WaterSeparatorRecipe;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class REIClient implements REIClientPlugin{
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new RocketStationCategory());
        registry.add(new WaterSeparatorCategory());
        registry.add(new FuelRefineryCategory());

        registry.addWorkstations(RocketStationCategory.ROCKET_CRAFTING, EntryStacks.of(BlocksRegistry.ROCKET_STATION.get()));
        registry.addWorkstations(WaterSeparatorCategory.WATER_SEPARATOR_CRAFTING, EntryStacks.of(BlocksRegistry.WATER_SEPARATOR.get()));
        registry.addWorkstations(FuelRefineryCategory.FUEL_REFINERY_CRAFTING, EntryStacks.of(BlocksRegistry.FUEL_REFINERY.get()));

    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(RocketStationRecipe.class, RocketStationRecipe.Type, RocketStationDisplay::new);
        registry.registerRecipeFiller(WaterSeparatorRecipe.class, WaterSeparatorRecipe.Type, WaterSeparatorDisplay::new);
        registry.registerRecipeFiller(FuelRefineryRecipe.class, FuelRefineryRecipe.Type, FuelRefineryDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        REIClientPlugin.super.registerScreens(registry);
    }
}
