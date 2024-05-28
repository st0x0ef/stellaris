package com.st0x0ef.stellaris.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.common.entities.LanderEntity;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerLanderRendererMixin {

    @Inject(at = @At(value = "HEAD"), method = "render", cancellable = true)
    private void render(AbstractClientPlayer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo info) {
        if(entity.getVehicle() instanceof LanderEntity) {
            info.cancel();
        }
    }
}
