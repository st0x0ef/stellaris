package com.st0x0ef.stellaris.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.st0x0ef.stellaris.client.skys.record.SkyPropertiesData;
import com.st0x0ef.stellaris.common.config.CustomConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.material.FogType;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class, priority = 900)
public abstract class LevelRendererMixin {
    @Mutable
    @Shadow
    private ClientLevel level;

    @Shadow
    protected abstract boolean doesMobEffectBlockSky(Camera camera);

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void renderCustomSkyboxes(PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable skyFogSetup, CallbackInfo ci) {
        FogType cameraSubmersionType = camera.getFluidInCamera();
        if ((boolean) CustomConfig.getValue("customSky") && !isFoggy && cameraSubmersionType != FogType.POWDER_SNOW && cameraSubmersionType != FogType.LAVA && cameraSubmersionType != FogType.WATER && !this.doesMobEffectBlockSky(camera) && SkyPropertiesData.SKY_PROPERTIES.containsKey(level.dimension())) {
            SkyPropertiesData.SKY_PROPERTIES.get(level.dimension()).getRenderer().render(Minecraft.getInstance().level, poseStack, projectionMatrix, partialTick, camera);
            ci.cancel();
        }
    }

    @Inject(method = "renderClouds", at = @At(value = "HEAD"), cancellable = true)
    private void cancelCloudRenderer(PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        if (SkyPropertiesData.SKY_PROPERTIES.containsKey(level.dimension())) {
            if(SkyPropertiesData.SKY_PROPERTIES.get(level.dimension()).getRenderer().shouldRemoveCloud()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderSnowAndRain", at = @At(value = "HEAD"), cancellable = true)
    private void cancelSnowAndRainRenderer(LightTexture lightTexture, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        if (SkyPropertiesData.SKY_PROPERTIES.containsKey(level.dimension())) {
            if(SkyPropertiesData.SKY_PROPERTIES.get(level.dimension()).getRenderer().shouldRemoveSnowAndRain()) {
                ci.cancel();
            }
        }
    }
}