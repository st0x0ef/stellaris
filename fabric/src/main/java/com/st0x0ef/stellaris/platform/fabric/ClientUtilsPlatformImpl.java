package com.st0x0ef.stellaris.platform.fabric;

import com.st0x0ef.stellaris.platform.ClientUtilsPlatform;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DyedItemColor;

public class ClientUtilsPlatformImpl {
    public static boolean IS_IRIS_INSTALLED = FabricLoader.getInstance().isModLoaded("iris");

    public static void registerArmor(ModelLayerLocation layer, ClientUtilsPlatform.ArmorFactory factory, Item... items) {
        ArmorRenderer.register((poseStack, buffer, stack, entity, slot, packedLight, original) -> {
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(layer);
            HumanoidModel<?> model = factory.create(root, slot, stack, original);
            if (stack.getItem() instanceof ArmorItem armorItem) {
                ArmorMaterial armorMaterial = armorItem.getMaterial().value();
                int i = stack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(stack, FastColor.ARGB32.color(250, 250, 250))) : -1;
                for (ArmorMaterial.Layer layer1 : armorMaterial.layers()) {
                    int j = layer1.dyeable() ? i : -1;
                    model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(layer1.texture(false))),
                            packedLight, OverlayTexture.NO_OVERLAY, j);
                }
            }
        }, items);
    }

    public static boolean isIrisInstalled() {
        return IS_IRIS_INSTALLED;
    }
}
