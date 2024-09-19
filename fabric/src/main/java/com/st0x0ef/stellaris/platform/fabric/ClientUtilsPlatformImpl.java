package com.st0x0ef.stellaris.platform.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.st0x0ef.stellaris.client.renderers.armors.SpaceSuitModel;
import com.st0x0ef.stellaris.platform.ClientUtilsPlatform;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.DyedItemColor;

public class ClientUtilsPlatformImpl {

    public static void registerArmor(ResourceLocation texture, ModelLayerLocation layer, ClientUtilsPlatform.ArmorFactory factory, Item... items) {
        ArmorRenderer.register((poseStack, buffer, stack, entity, slot, packedLight, original) -> {
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(layer);
            HumanoidModel<?> model = factory.create(root, slot, stack, original);

            model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(texture)), packedLight, OverlayTexture.NO_OVERLAY, -1);
        }, items);
    }

    public static void registerDyeableArmor(ModelLayerLocation layer, ClientUtilsPlatform.ArmorFactory factory, Item... items) {
        ArmorRenderer.register((poseStack, buffer, stack, entity, slot, packedLight, original) -> {
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(layer);
            SpaceSuitModel model = (SpaceSuitModel) factory.create(root, slot, stack, original);
            if (stack.getItem() instanceof ArmorItem armorItem) {
                ArmorMaterial armorMaterial = armorItem.getMaterial().value();
                int i = stack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(stack, -6265536)) : -1;
                for (ArmorMaterial.Layer layer1 : armorMaterial.layers()) {
                    int j = layer1.dyeable() ? i : -1;
                    renderModel(poseStack, buffer, packedLight, model, j, layer1.texture(false));
                }
            }
        }, items);
    }

    private static <A extends HumanoidModel<?>> void renderModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, A model, int dyeColor, ResourceLocation textureLocation) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(textureLocation));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, dyeColor);
    }
}
