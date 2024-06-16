package com.st0x0ef.stellaris.platform.neoforge;

import com.st0x0ef.stellaris.platform.ClientUtilsPlatform;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ClientUtilsPlatformImpl {

    public static final Map<Item, ArmorRenderer> ARMOR_RENDERERS = new HashMap<>();


    public static void registerArmor(ResourceLocation texture, ModelLayerLocation layer, ClientUtilsPlatform.ArmorFactory factory, Item... items) {
        for (Item item : items) {
            ARMOR_RENDERERS.put(item, new ArmorRenderer(texture, layer, factory));
        }
    }


    public record ArmorRenderer(ResourceLocation texture, ModelLayerLocation layer,
                                ClientUtilsPlatform.ArmorFactory factory) {}


}
