package com.st0x0ef.stellaris.common.compats.jei;

import com.st0x0ef.stellaris.client.screens.tablet.TabletMainScreen;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.registry.RecipesRegistry;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static final ResourceLocation ID = ResourceLocationUtils.id("jei");

    public JEIPlugin () {
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new RocketStationCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        ClientLevel level = Minecraft.getInstance().level;

        registry.addRecipes(RocketStationCategory.RECIPE, level.getRecipeManager().getAllRecipesFor(RecipesRegistry.ROCKET_STATION_TYPE.get()).stream().map(RecipeHolder::value).toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(ItemsRegistry.ROCKET_STATION.get().getDefaultInstance(), RocketStationCategory.RECIPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(TabletMainScreen.class, new IGuiContainerHandler<TabletMainScreen>() {
            @Override
            public @NotNull List<Rect2i> getGuiExtraAreas(TabletMainScreen containerScreen) {
                return List.of(new Rect2i(0, 0, containerScreen.width, containerScreen.height));
            }
        });

    }

}
