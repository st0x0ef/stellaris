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

    public void render(ClientLevel level, PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera) {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();

        CustomVanillaObject customVanillaObject = properties.customVanillaObject();

        float dayAngle = level.getTimeOfDay(partialTick) * 360f % 360f;

        Vec3 vec3 = level.getSkyColor(camera.getPosition(), partialTick);
        float r = (float) vec3.x;
        float g = (float) vec3.y;
        float b = (float) vec3.z;

        FogRenderer.levelFogColor();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(r, g, b, 1.0f);

        ShaderInstance shaderInstance = RenderSystem.getShader();
        SkyHelper.drawSky(poseStack.last().pose(), projectionMatrix, shaderInstance);

        // Star
        renderStars(level, dayAngle, partialTick, poseStack, projectionMatrix, camera);

        // Sun
        if (customVanillaObject.sun()) {
            SkyHelper.drawCelestialBody(customVanillaObject.sunTexture(), bufferBuilder, poseStack, 100f, 30f, dayAngle, true);
        }

        // Moon
        if (customVanillaObject.moon()) {
            if (customVanillaObject.moonPhase()) {
                SkyHelper.drawMoonWithPhase(level, bufferBuilder, poseStack, -100f, customVanillaObject, dayAngle);
            } else {
                SkyHelper.drawCelestialBody(customVanillaObject.moonTexture(), bufferBuilder, poseStack, -100f, 20f, dayAngle, false);
            }
        }

        // Other sky object
        for (SkyObject skyObject : properties.skyObjects()) {
            SkyHelper.drawCelestialBody(skyObject, bufferBuilder, poseStack, 100f, dayAngle, skyObject.blend());
        }

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.depthMask(true);
    }

    private void renderStars(ClientLevel level, float dayAngle, float partialTick, PoseStack poseStack, Matrix4f projectionMatrix, Camera camera) {
        float rainLevel = 1.0F - level.getRainLevel(partialTick);
        float starLight = level.getStarBrightness(partialTick) * rainLevel;
        if (starLight > 0.0F) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(starLight + 0.5F, starLight + 0.5F, starLight + 0.5F, starLight + 0.5F);
            StarHelper.drawStars(starBuffer, poseStack, projectionMatrix, camera, dayAngle);
        } else if(properties.stars().allDaysVisible()){
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(starLight + 1F, starLight + 1F, starLight + 1F, starLight + 1F);
            StarHelper.drawStars(starBuffer, poseStack, projectionMatrix, camera, dayAngle);
        }
    }

    public Boolean shouldRemoveCloud() {
        return !properties.cloud();
    }

    public Boolean shouldRemoveSnowAndRain() {
        return properties.weather().isEmpty();
    }
}
