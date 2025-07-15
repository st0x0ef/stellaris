package com.st0x0ef.stellaris.platform.neoforge;

import com.st0x0ef.stellaris.platform.ClientUtilsPlatform;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;

import java.util.HashMap;
import java.util.Map;

public class ClientUtilsPlatformImpl {
    public static boolean IS_IRIS_INSTALLED = ModList.get().isLoaded("iris");

    public static final Map<Item, ArmorRenderer> ARMOR_RENDERERS = new HashMap<>();

    public static void registerArmor(ModelLayerLocation layer, ClientUtilsPlatform.ArmorFactory factory, Item... items) {
        for (Item item : items) {
            ARMOR_RENDERERS.put(item, new ArmorRenderer(layer, factory));
        }
    }

    public record ArmorRenderer(ModelLayerLocation layer, ClientUtilsPlatform.ArmorFactory factory) {

    }


    public static boolean isIrisInstalled() {
        return IS_IRIS_INSTALLED;
    }
}
