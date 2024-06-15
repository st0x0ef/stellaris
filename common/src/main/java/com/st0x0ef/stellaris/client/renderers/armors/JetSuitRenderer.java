package com.st0x0ef.stellaris.client.renderers.armors;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.items.JetSuitItem;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class JetSuitRenderer extends EntityRenderer<Player> {
    private final HumanoidModel<Player> model;

    public JetSuitRenderer(EntityRendererProvider.Context context, HumanoidModel<Player> model) {
        super(context);
        this.model = model;
    }

    @Override
    public void render(Player entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
        this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Player entity) {
        return JetSuitModel.TEXTURE;
    }

    public static class JetSuitLayer<T extends Player, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
        public JetSuitLayer(RenderLayerParent<T, M> entityRendererIn) {
            super(entityRendererIn);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack chest = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
            if (chest.getItem() instanceof JetSuit.Suit) {
                poseStack.pushPose();
                this.getParentModel().setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(this.getRenderType(this.getTexture(livingEntity))), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                poseStack.popPose();
            }
        }

        @ExpectPlatform
        private RenderType getRenderType(ResourceLocation texture) {
            throw new AssertionError();
        }

        @ExpectPlatform
        private ResourceLocation getTexture(T livingEntity) {
            throw new AssertionError();
        }
    }
}