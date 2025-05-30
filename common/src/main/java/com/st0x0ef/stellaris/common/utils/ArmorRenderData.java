package com.st0x0ef.stellaris.common.utils;

import com.st0x0ef.stellaris.client.renderers.armors.JetSuitModel;
import com.st0x0ef.stellaris.client.renderers.armors.SpaceSuitModel;
import com.st0x0ef.stellaris.common.items.armors.AbstractSpaceArmor;
import com.st0x0ef.stellaris.common.items.armors.JetSuit;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiFunction;

public record ArmorRenderData(ModelLayerLocation layer, ResourceLocation texture,
                              BiFunction<ModelPart, ItemStack, HumanoidModel<?>> modelProvider) {

    public static final ArmorRenderData JET_SUIT = new ArmorRenderData(JetSuitModel.LAYER_LOCATION, JetSuitModel.TEXTURE,
            (modelPart, stack) -> new JetSuitModel(modelPart, EquipmentSlot.CHEST, stack, null));
    public static final ArmorRenderData SPACE_SUIT = new ArmorRenderData(SpaceSuitModel.LAYER_LOCATION, SpaceSuitModel.TEXTURE,
            (modelPart, stack) -> new SpaceSuitModel(modelPart, EquipmentSlot.CHEST, stack, null));

    public static ArmorRenderData get(ItemStack stack) {
        if (stack.getItem() instanceof JetSuit.Suit) {
            return JET_SUIT;
        }
        else if (stack.getItem() instanceof AbstractSpaceArmor) {
            return SPACE_SUIT;
        }
        return null;
    }
}
