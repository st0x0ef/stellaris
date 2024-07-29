package com.st0x0ef.stellaris.client.skys.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Matrix4f;

public class SkyRenderer {

    private final SkyProperties properties;
    private VertexBuffer starBuffer;

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

        float dayTime = level.getTimeOfDay(partialTick);
        float dayAngle = dayTime * 360f % 360f;

        FogType cameraSubmersionType = camera.getFluidInCamera();
        if (cameraSubmersionType.equals(FogType.POWDER_SNOW) || cameraSubmersionType.equals(FogType.LAVA) || mc.levelRenderer.doesMobEffectBlockSky(camera)) {
            return;
        }

        Vec3 vec3 = mc.level.getSkyColor(mc.gameRenderer.getMainCamera().getPosition(), partialTick);
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

        Matrix4f matrix4f2 = poseStack.last().pose();

        // Start
        renderStars(dayAngle, partialTick, poseStack, projectionMatrix);

        // Sun
        this.renderSun(bufferBuilder, poseStack, matrix4f2, dayAngle, customVanillaObject);

        // Moon
        if (customVanillaObject.moon()) {
            this.renderMoon(bufferBuilder, matrix4f2, customVanillaObject);
        }

        for (SkyObject skyObject : this.properties.skyObjects()) {
            renderSkyObject(bufferBuilder, poseStack, skyObject);
        }

        SkyHelper.setupShaderColor(mc, r, g, b);
        RenderSystem.depthMask(true);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

//    public void render(ClientLevel level, PoseStack poseStack, Matrix4f matrix4f, float tickDelta) {
//        if (level.dimension() != properties.id() || poseStack == null) return;
//
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        //RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//        VertexBuffer.unbind();
//
//        poseStack.pushPose();
//
//        Matrix4f matrix4f2 = poseStack.last().pose();
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
//
//        CustomVanillaObject customVanillaObject = this.properties.customVanillaObject();
//
//        // Stars
//        this.renderStars(Math.clamp(level.getTimeOfDay(tickDelta) * 360f, 0f, 360f), tickDelta, poseStack, matrix4f);
//
//        // Sun
//        if (customVanillaObject.sun()) {
//            this.renderSun(bufferBuilder, matrix4f2, customVanillaObject);
//        }
//
//        // Moon
//        if (customVanillaObject.moon()) {
//            this.renderMoon(bufferBuilder, matrix4f2, customVanillaObject);
//        }
//
//        for (SkyObject skyObject : this.properties.skyObjects()) {
//            renderSkyObject(bufferBuilder, poseStack, skyObject);
//        }
//
//        poseStack.popPose();
//
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.disableBlend();
//    }

    public void renderSun(BufferBuilder bufferBuilder, PoseStack poseStack, Matrix4f matrix4f, float dayAngle, CustomVanillaObject customVanillaObject) {
        //RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, customVanillaObject.sunTexture());
//        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        bufferBuilder.vertex(matrix4f, -25.0F, 100.0F, -25.0F).uv(1.0F, 0.0F).endVertex();
//        bufferBuilder.vertex(matrix4f, 25.0F, 100.0F, -25.0F).uv(0.0F, 0.0F).endVertex();
//        bufferBuilder.vertex(matrix4f, 25.0F, 100.0F, 25.0F).uv(0.0F, 1.0F).endVertex();
//        bufferBuilder.vertex(matrix4f, -25.0F, 100.0F, 25.0F).uv(1.0F, 1.0F).endVertex();
//        BufferUploader.drawWithShader(bufferBuilder.end());

        SkyHelper.drawCelestialBody(customVanillaObject.sunTexture(), new Vec3(255, 255, 255), bufferBuilder, poseStack, matrix4f, 50f, 100f, dayAngle, true);
    }

    public void renderSkyObject(BufferBuilder bufferBuilder, PoseStack poseStack, SkyObject skyObject) {
        poseStack.pushPose();
        RenderSystem.enableBlend();

        poseStack.mulPose(Axis.XP.rotationDegrees((float) skyObject.rotation().x));
        poseStack.mulPose(Axis.YP.rotationDegrees((float) skyObject.rotation().y));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) skyObject.rotation().z));

        Matrix4f matrix = poseStack.last().pose();
        float size = skyObject.size();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, skyObject.texture());
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix, -size, 100, -size).uv(1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix, size, 100, -size).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix, size, 100, size).uv(0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(matrix, -size, 100, size).uv(1.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
        poseStack.popPose();
        RenderSystem.disableBlend();
    }



    public void renderMoon(BufferBuilder bufferBuilder, Matrix4f matrix4f, CustomVanillaObject customVanillaObject) {
        float startX = 0;
        float startY = 1;
        float endX = 1;
        float endY = 0;

        if (customVanillaObject.moonTexture().equals(new ResourceLocation("textures/environment/moon_phases.png"))) {
            int moonPhase = Minecraft.getInstance().level.getMoonPhase();
            int xCoord = moonPhase % 4;
            int yCoord = moonPhase / 4 % 2;
            startX = xCoord / 4.0F;
            startY = yCoord / 2.0F;
            endX = (xCoord + 1) / 4.0F;
            endY = (yCoord + 1) / 2.0F;
        }

        RenderSystem.setShaderTexture(0, customVanillaObject.moonTexture());

        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, -16.0F, -100.0F, 16.0F).uv(endX, endY).endVertex();
        bufferBuilder.vertex(matrix4f, 16.0F, -100.0F, 16.0F).uv(startX, endY).endVertex();
        bufferBuilder.vertex(matrix4f, 16.0F, -100.0F, -16.0F).uv(startX, startY).endVertex();
        bufferBuilder.vertex(matrix4f, -16.0F, -100.0F, -16.0F).uv(endX, startY).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    private void renderStars(float dayAngle, float partialTick, PoseStack poseStack, Matrix4f projectionMatrix) {
        float rainLevel = 1.0F - Minecraft.getInstance().level.getRainLevel(partialTick);
        float starLight = Minecraft.getInstance().level.getStarBrightness(partialTick) * rainLevel;
        if (starLight > 0.0F) {
            Matrix4f matrix4f = SkyHelper.setMatrixRot(poseStack, Triple.of(Axis.YP.rotationDegrees(-90), Axis.XP.rotationDegrees(dayAngle), null));
            RenderSystem.setShaderColor(starLight + 0.5F, starLight + 0.5F, starLight + 0.5F, starLight + 0.5F);
            SkyHelper.drawStars(starBuffer, matrix4f, projectionMatrix, GameRenderer.getPositionColorShader(), true);
        } else if(this.properties.stars().allDaysVisible()){
            Matrix4f matrix4f = SkyHelper.setMatrixRot(poseStack, Triple.of(Axis.YP.rotationDegrees(-90), Axis.XP.rotationDegrees(dayAngle), null));
            RenderSystem.setShaderColor(starLight + 1F, starLight + 1F, starLight + 1F, starLight + 1F);
            SkyHelper.drawStars(starBuffer, matrix4f, projectionMatrix, GameRenderer.getPositionColorShader(), true);
        }
    }

    public void renderCloud() {
        // No cloud on planet, so we do nothing here.
    }
}
