package com.st0x0ef.stellaris.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.client.renderers.armors.JetSuitModel;
import com.st0x0ef.stellaris.client.renderers.armors.SpaceSuitModel;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class ArmRendererMixin {

    @Inject(method = "renderHand*", at = @At("HEAD"), cancellable = true)
    private void renderJetSuitArm(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ResourceLocation skinTexture, ModelPart arm, boolean isSleeveVisible, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);

        if (Utils.isJetSuitPart(stack)) {
            ModelLayerLocation layer = JetSuitModel.LAYER_LOCATION;
            ResourceLocation texture = JetSuitModel.TEXTURE;
            ModelPart rootPart = Minecraft.getInstance().getEntityModels().bakeLayer(layer);
            JetSuitModel model = new JetSuitModel(rootPart, EquipmentSlot.CHEST, stack, null);

            if (arm == model.rightArm) {
                model.rightArm.copyFrom(arm);
                model.rightArm.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(texture)), packedLight, OverlayTexture.NO_OVERLAY);
            } else {
                model.leftArm.copyFrom(arm);
                model.leftArm.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(texture)), packedLight, OverlayTexture.NO_OVERLAY);

            }

            ci.cancel();
        }

        if (Utils.isSpaceSuitPart(stack)) {
            ModelLayerLocation layer = SpaceSuitModel.LAYER_LOCATION;
            ResourceLocation texture = SpaceSuitModel.TEXTURE;
            ModelPart rootPart = Minecraft.getInstance().getEntityModels().bakeLayer(layer);
            SpaceSuitModel model = new SpaceSuitModel(rootPart, EquipmentSlot.CHEST, stack, null);

            if (arm == model.rightArm) {
                model.rightArm.copyFrom(arm);
                model.rightArm.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(texture)), packedLight, OverlayTexture.NO_OVERLAY);
            } else {
                model.leftArm.copyFrom(arm);
                model.leftArm.render(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(texture)), packedLight, OverlayTexture.NO_OVERLAY);

            }

            ci.cancel();
        }
    }
}