package com.st0x0ef.stellaris.platform;

import com.st0x0ef.stellaris.client.skys.renderer.SkyRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.msrandom.multiplatform.annotations.Actual;

import java.util.HashMap;
import java.util.Map;

public class PlatformClientUtilsHelper {

    public static final Map<Item, ArmorRenderer> ARMOR_RENDERERS = new HashMap<>();
    public static final Map<ResourceKey<Level>, SkyRenderer> DIMENSION_RENDERERS = new HashMap<>();

    public static void registerArmor(ResourceLocation texture, ModelLayerLocation layer, ArmorFactory factory, Item... items) {
        for (Item item : items) {
            ARMOR_RENDERERS.put(item, new ArmorRenderer(texture, layer, factory));
        }
    }

    public static void registerPlanetsSkies(Map<ResourceKey<Level>, SkyRenderer> renderer) {
        DIMENSION_RENDERERS.clear();
        DIMENSION_RENDERERS.putAll(renderer);
    }


    public record ArmorRenderer(ResourceLocation texture, ModelLayerLocation layer,
                                ArmorFactory factory) {}


}
