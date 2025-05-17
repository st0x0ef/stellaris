package com.st0x0ef.stellaris.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.common.entities.vehicles.LanderEntity;
import com.st0x0ef.stellaris.common.items.armors.AbstractSpaceArmor;
import com.st0x0ef.stellaris.common.items.armors.JetSuit;
import com.st0x0ef.stellaris.common.items.armors.SpaceSuit;
import com.st0x0ef.stellaris.common.utils.ArmorRenderData;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/PlayerModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER), cancellable = true)
    private void renderPlayerHand(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, ModelPart renderedArm, ModelPart rendererArmSleeve, CallbackInfo ci) {
        ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);

        if (stack.getItem() instanceof JetSuit.Suit || (stack.getItem() instanceof AbstractSpaceArmor)) {
            ci.cancel();

            ArmorRenderData data = ArmorRenderData.get(stack);
            ModelLayerLocation layer = data.layer();
            ResourceLocation texture = data.texture();
            ModelPart rootPart = Minecraft.getInstance().getEntityModels().bakeLayer(layer);
            HumanoidModel<?> model = data.modelProvider().apply(rootPart, stack);

            if (renderedArm == model.rightArm) {
                model.rightArm.copyFrom(renderedArm);
                model.rightArm.render(poseStack, buffer.getBuffer(RenderType.entityTranslucent(texture)), packedLight, OverlayTexture.NO_OVERLAY);
                return;
            }

            model.leftArm.copyFrom(renderedArm);
            model.leftArm.render(poseStack, buffer.getBuffer(RenderType.entityTranslucent(texture)), packedLight, OverlayTexture.NO_OVERLAY);
        }
    }

    @Inject(method = "render*", at = @At("HEAD"), cancellable = true)
    public void renderPlayer(AbstractClientPlayer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity.getVehicle() instanceof LanderEntity) {
            ci.cancel();
        }

        if (Utils.isLivingInSpaceSuit(entity)) {
            ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);

            if (stack.getItem() instanceof SpaceSuit spaceSuitItem) {
                spaceSuitItem.getModules(stack).forEach(module -> module.renderModel(poseStack, buffer, entity, entityYaw, partialTicks, packedLight));
            }
        }
    }
}