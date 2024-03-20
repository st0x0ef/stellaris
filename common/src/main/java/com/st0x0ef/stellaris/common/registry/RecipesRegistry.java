package com.st0x0ef.stellaris.common.registry;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.recipes.RocketStationRecipe;
import com.st0x0ef.stellaris.common.menus.RocketStationMenu;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipesRegistry {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Stellaris.MODID, Registries.RECIPE_TYPE);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Stellaris.MODID, Registries.RECIPE_SERIALIZER);


    public static final RegistrySupplier<RecipeType<RocketStationRecipe>> ROCKET_STATION_TYPE = RECIPE_TYPES.register("rocket_station",
            () -> new RocketStationRecipe.Type()
            {
                @Override
                public String toString()
                {
                    return "rocket_station";
                }
            });


    public static final RegistrySupplier<RecipeSerializer<RocketStationRecipe>> ROCKET_STATION = RECIPE_SERIALIZERS.register(
            "rocket_station",
            RocketStationRecipe.Serializer::new
    );

    public static void register() {
        RECIPE_TYPES.register();
        RECIPE_SERIALIZERS.register();
    }

}
