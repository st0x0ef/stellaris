package com.st0x0ef.stellaris.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.client.renderers.armors.JetSuitModel;
import com.st0x0ef.stellaris.client.renderers.armors.SpaceSuitModel;
import com.st0x0ef.stellaris.common.items.armors.SpaceSuit;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class ArmorRendererMixin {
    @Shadow @Final private EquipmentLayerRenderer equipmentRenderer;

    @Shadow protected abstract void renderArmorPiece(PoseStack poseStack, MultiBufferSource bufferSource, ItemStack armorItem, EquipmentSlot slot, int packedLight, HumanoidModel<?> model);

    @Inject(method = "render*", at = @At("HEAD"))
    private void renderArmor(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, HumanoidRenderState humanoidRenderState, float f, float g, CallbackInfo ci) {
        // JET SUIT
        ModelPart jetSuitRootPart = Minecraft.getInstance().getEntityModels().bakeLayer(JetSuitModel.LAYER_LOCATION);

        if (Utils.isJetSuitPart(humanoidRenderState.feetEquipment)) {
            renderArmorPiece(poseStack, bufferSource, humanoidRenderState.feetEquipment, EquipmentSlot.FEET, packedLight, new JetSuitModel(jetSuitRootPart, EquipmentSlot.FEET, humanoidRenderState.feetEquipment, null));
        }

        if (Utils.isJetSuitPart(humanoidRenderState.legsEquipment)) {
            renderArmorPiece(poseStack, bufferSource, humanoidRenderState.legsEquipment, EquipmentSlot.LEGS, packedLight, new JetSuitModel(jetSuitRootPart, EquipmentSlot.LEGS, humanoidRenderState.legsEquipment, null));
        }

        if (Utils.isJetSuitPart(humanoidRenderState.chestEquipment)) {
            renderArmorPiece(poseStack, bufferSource, humanoidRenderState.chestEquipment, EquipmentSlot.CHEST, packedLight, new JetSuitModel(jetSuitRootPart, EquipmentSlot.CHEST, humanoidRenderState.chestEquipment, null));
        }

        if (Utils.isJetSuitPart(humanoidRenderState.headEquipment)) {
            renderArmorPiece(poseStack, bufferSource, humanoidRenderState.headEquipment, EquipmentSlot.HEAD, packedLight, new JetSuitModel(jetSuitRootPart, EquipmentSlot.HEAD, humanoidRenderState.headEquipment, null));
        }

        // SPACE SUIT
        ModelPart spaceSuitRootPart = Minecraft.getInstance().getEntityModels().bakeLayer(SpaceSuitModel.LAYER_LOCATION);

        if (Utils.isSpaceSuitPart(humanoidRenderState.feetEquipment)) {
            renderArmorPiece(poseStack, bufferSource, humanoidRenderState.feetEquipment, EquipmentSlot.FEET, packedLight, new SpaceSuitModel(spaceSuitRootPart, EquipmentSlot.FEET, humanoidRenderState.feetEquipment, null));
        }

        if (Utils.isSpaceSuitPart(humanoidRenderState.legsEquipment)) {
            renderArmorPiece(poseStack, bufferSource, humanoidRenderState.legsEquipment, EquipmentSlot.LEGS, packedLight, new SpaceSuitModel(spaceSuitRootPart, EquipmentSlot.LEGS, humanoidRenderState.legsEquipment, null));
        }

        if (Utils.isSpaceSuitPart(humanoidRenderState.chestEquipment)) {
            renderArmorPiece(poseStack, bufferSource, humanoidRenderState.chestEquipment, EquipmentSlot.CHEST, packedLight, new SpaceSuitModel(spaceSuitRootPart, EquipmentSlot.CHEST, humanoidRenderState.chestEquipment, null));
        }

        if (Utils.isSpaceSuitPart(humanoidRenderState.headEquipment)) {
            renderArmorPiece(poseStack, bufferSource, humanoidRenderState.headEquipment, EquipmentSlot.HEAD, packedLight, new SpaceSuitModel(spaceSuitRootPart, EquipmentSlot.HEAD, humanoidRenderState.headEquipment, null));
        }

        if (Utils.isSpaceSuitPart(humanoidRenderState.chestEquipment)) {
            if (humanoidRenderState.chestEquipment.getItem() instanceof SpaceSuit spaceSuitItem) {
                spaceSuitItem.getModules(humanoidRenderState.chestEquipment).forEach(module -> module.renderModel(poseStack, bufferSource, humanoidRenderState, packedLight));
            }
        }
    }
}