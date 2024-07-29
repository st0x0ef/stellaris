package com.st0x0ef.stellaris.client.skys.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.st0x0ef.stellaris.client.skys.record.CustomVanillaObject;
import com.st0x0ef.stellaris.client.skys.record.SkyObject;
import com.st0x0ef.stellaris.client.skys.record.SkyProperties;
import com.st0x0ef.stellaris.client.skys.utils.SkyHelper;
import com.st0x0ef.stellaris.client.skys.utils.StarHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
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
        Matrix4f matrix4f = poseStack.last().pose();
        ShaderInstance shaderInstance = RenderSystem.getShader();
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        Camera camera = mc.gameRenderer.getMainCamera();
        CustomVanillaObject customVanillaObject = this.properties.customVanillaObject();

        float dayAngle = level.getTimeOfDay(partialTick) * 360f % 360f;

        FogType cameraSubmersionType = camera.getFluidInCamera();
        if (cameraSubmersionType.equals(FogType.POWDER_SNOW) || cameraSubmersionType.equals(FogType.LAVA) || mc.levelRenderer.doesMobEffectBlockSky(camera)) {
            return;
        }

        Vec3 vec3 = mc.level.getSkyColor(camera.getPosition(), partialTick);
        float r = (float) vec3.x;
        float g = (float) vec3.y;
        float b = (float) vec3.z;

        FogRenderer.levelFogColor();
        RenderSystem.depthMask(false);

        RenderSystem.setShaderColor(r, g, b, 1.0f);
        SkyHelper.drawSky(mc, matrix4f, projectionMatrix, shaderInstance);
        VertexBuffer.unbind();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Star
        renderStars(dayAngle, partialTick, poseStack, projectionMatrix, camera);

        // Sun
        if (customVanillaObject.sun()) {
            SkyHelper.drawCelestialBody(customVanillaObject.sunTexture(), bufferBuilder, poseStack, camera, 30f, dayAngle, true);
        }

        // Moon
        if (customVanillaObject.moon()) {
            SkyHelper.drawMoonWithPhase(bufferBuilder, poseStack, camera,customVanillaObject, (dayAngle + 180 % 360));
        }

        // Other sky object
        for (SkyObject skyObject : this.properties.skyObjects()) {
            SkyHelper.drawCelestialBody(skyObject, bufferBuilder, poseStack, camera, dayAngle, true);
        }

        SkyHelper.setupShaderColor(mc, r, g, b);
        RenderSystem.depthMask(true);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void renderStars(float dayAngle, float partialTick, PoseStack poseStack, Matrix4f projectionMatrix, Camera camera) {
        float rainLevel = 1.0F - Minecraft.getInstance().level.getRainLevel(partialTick);
        float starLight = Minecraft.getInstance().level.getStarBrightness(partialTick) * rainLevel;
        if (starLight > 0.0F) {
            RenderSystem.setShaderColor(starLight + 0.5F, starLight + 0.5F, starLight + 0.5F, starLight + 0.5F);
            SkyHelper.drawStars(starBuffer, poseStack, projectionMatrix, GameRenderer.getPositionColorShader(), camera, dayAngle, true);
        } else if(this.properties.stars().allDaysVisible()){
            RenderSystem.setShaderColor(starLight + 1F, starLight + 1F, starLight + 1F, starLight + 1F);
            SkyHelper.drawStars(starBuffer, poseStack, projectionMatrix, GameRenderer.getPositionColorShader(), camera, dayAngle, true);
        }
    }

    public void removeCloud() {
        // Doing nothing remove cloud
    }

    public void removeSnowAndRain() {
        // Doing nothing remove snow and rain
    }
}
