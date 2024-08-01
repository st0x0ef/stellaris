package com.st0x0ef.stellaris.client.skys.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.st0x0ef.stellaris.client.skys.record.CustomVanillaObject;
import com.st0x0ef.stellaris.client.skys.record.SkyObject;
import com.st0x0ef.stellaris.client.skys.record.SkyProperties;
import com.st0x0ef.stellaris.client.skys.utils.SkyHelper;
import com.st0x0ef.stellaris.client.skys.utils.StarHelper;
import com.st0x0ef.stellaris.mixin.client.LevelRendererAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class SkyRenderer {

    private final SkyProperties properties;
    private final VertexBuffer starBuffer;

    public SkyRenderer(SkyProperties properties) {
        this.properties = properties;

        if (properties.stars().colored()) {
            starBuffer = StarHelper.createStars(0.1F, properties.stars().count(), 190, 160, -1);
        } else {
            starBuffer = StarHelper.createStars(0.1F, properties.stars().count(), 255, 255, 255);
        }
    }

    public void render(ClientLevel level, float partialTick, PoseStack poseStack, Matrix4f projectionMatrix) {
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();

        FogType cameraSubmersionType = camera.getFluidInCamera();
        if (cameraSubmersionType.equals(FogType.POWDER_SNOW) || cameraSubmersionType.equals(FogType.LAVA) || mc.levelRenderer.doesMobEffectBlockSky(camera)) return;

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        ShaderInstance shaderInstance = RenderSystem.getShader();
        CustomVanillaObject customVanillaObject = this.properties.customVanillaObject();
        VertexBuffer darkBuffer = ((LevelRendererAccessor) Minecraft.getInstance().levelRenderer).stellaris$getDarkBuffer();

        float dayAngle = level.getTimeOfDay(partialTick) * 360f % 360f;
        float sunAngle = Mth.sin(level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;

        Vec3 vec3 = level.getSkyColor(camera.getPosition(), partialTick);
        float r = (float) vec3.x;
        float g = (float) vec3.y;
        float b = (float) vec3.z;

        FogRenderer.levelFogColor();
        RenderSystem.depthMask(false);

        RenderSystem.setShaderColor(r, g, b, 1.0f);
        SkyHelper.drawSky(mc, poseStack.last().pose(), projectionMatrix, shaderInstance);

        // Star
        renderStars(level, dayAngle, partialTick, poseStack, projectionMatrix, camera);

        // Sun
        if (customVanillaObject.sun()) {
            SkyHelper.drawCelestialBody(customVanillaObject.sunTexture(), bufferBuilder, poseStack, 100f, 30f, sunAngle, true);
        }

        // Moon
        if (customVanillaObject.moon()) {
            SkyHelper.drawMoonWithPhase(level, bufferBuilder, poseStack, -100f, customVanillaObject, dayAngle);
        }

        // Other sky object
        for (SkyObject skyObject : this.properties.skyObjects()) {
            SkyHelper.drawCelestialBody(skyObject, bufferBuilder, poseStack, 100f, dayAngle, skyObject.blend());
        }

        double d = mc.player.getEyePosition(partialTick).y - level.getLevelData().getHorizonHeight(level);
        if (d < 0.0) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 12.0F, 0.0F);

            darkBuffer.bind();
            darkBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderInstance);
            VertexBuffer.unbind();
            poseStack.popPose();
        }

        RenderSystem.depthMask(true);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderStars(ClientLevel level, float dayAngle, float partialTick, PoseStack poseStack, Matrix4f projectionMatrix, Camera camera) {
        float rainLevel = 1.0F - level.getRainLevel(partialTick);
        float starLight = level.getStarBrightness(partialTick) * rainLevel;
        if (starLight > 0.0F) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(starLight + 0.5F, starLight + 0.5F, starLight + 0.5F, starLight + 0.5F);
            StarHelper.drawStars(starBuffer, poseStack, projectionMatrix, camera, dayAngle);
        } else if(this.properties.stars().allDaysVisible()){
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(starLight + 1F, starLight + 1F, starLight + 1F, starLight + 1F);
            StarHelper.drawStars(starBuffer, poseStack, projectionMatrix, camera, dayAngle);
        }
    }

    public void removeCloud() {
        // Doing nothing remove cloud
    }

    public void removeSnowAndRain() {
        // Doing nothing remove snow and rain
    }
}
