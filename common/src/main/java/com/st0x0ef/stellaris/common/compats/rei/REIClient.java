package com.st0x0ef.stellaris.common.compats.rei;

import com.st0x0ef.stellaris.client.screens.tablet.TabletMainScreen;
import com.st0x0ef.stellaris.common.data.recipes.RocketStationRecipe;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.List;

public class REIClient implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new RocketStationCategory());

        registry.addWorkstations(RocketStationCategory.ROCKET_CRAFTING, EntryStacks.of(BlocksRegistry.ROCKET_STATION.get()));
    }

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(TabletMainScreen.class, (screen) -> {
            Rectangle rectangle = new Rectangle(0, 0, screen.width, screen.height);
            return List.of(rectangle);
        });
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(RocketStationRecipe.class, RocketStationRecipe.Type, RocketStationDisplay::new);
    }
}