package com.st0x0ef.stellaris.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.client.skys.record.SkyPropertiesData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin  {

    @Mutable
    @Shadow
    private ClientLevel level;

    @Inject(method = "renderClouds", at = @At(value = "HEAD"), cancellable = true)
    private void canCloudRenderer(PoseStack poseStack, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        if (SkyPropertiesData.SKY_PROPERTIES.containsKey(level.dimension())) {
            if(!SkyPropertiesData.SKY_PROPERTIES.get(level.dimension()).hasCloud()) {
                ci.cancel();
            }
        }
    }

}
