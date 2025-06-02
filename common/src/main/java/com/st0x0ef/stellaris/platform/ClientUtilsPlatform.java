package com.st0x0ef.stellaris.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ClientUtilsPlatform {
    @FunctionalInterface
    public interface ArmorFactory {
        HumanoidModel<?> create(ModelPart root, EquipmentSlot slot, ItemStack stack, HumanoidModel<HumanoidRenderState> parentModel);
    }

    @ExpectPlatform
    public static void registerArmor(ModelLayerLocation layer, ArmorFactory factory, ResourceLocation resourceLocation, Item... items) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isIrisInstalled() {
        throw new AssertionError();
    }
}
