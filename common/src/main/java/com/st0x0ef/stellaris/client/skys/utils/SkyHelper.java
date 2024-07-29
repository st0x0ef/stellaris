package com.st0x0ef.stellaris.client.skys.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.client.skys.record.CustomVanillaObject;
import com.st0x0ef.stellaris.client.skys.record.SkyObject;
import com.st0x0ef.stellaris.mixin.client.LevelRendererAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class SkyHelper {
    public static void setupShaderColor(Minecraft mc, float r, float g, float b) {
        if (mc.level.effects().hasGround()) {
            RenderSystem.setShaderColor(r * 0.2F + 0.04F, g * 0.2F + 0.04F, b * 0.6F + 0.1F, 1.0F);
        } else {
            RenderSystem.setShaderColor(r, g, b, 1.0F);
        }
    }

    public static Matrix4f setMatrixRot(PoseStack poseStack, Triple<Quaternionf, Quaternionf, Quaternionf> quaternionTriple) {
        poseStack.pushPose();

        Quaternionf left = quaternionTriple.getLeft();
        Quaternionf middle = quaternionTriple.getMiddle();
        Quaternionf right = quaternionTriple.getRight();

        if (left != null) {
            poseStack.mulPose(quaternionTriple.getLeft());
        }

        if (middle != null) {
            poseStack.mulPose(quaternionTriple.getMiddle());
        }

        if (right != null) {
            poseStack.mulPose(quaternionTriple.getRight());
        }

        Matrix4f matrix4f = poseStack.last().pose();
        poseStack.popPose();

        return matrix4f;
    }

    public static void drawSky(Minecraft mc, Matrix4f matrix4f, Matrix4f projectionMatrix, ShaderInstance shaderInstance) {
        ((LevelRendererAccessor) mc.levelRenderer).stellaris$getSkyBuffer().bind();
        ((LevelRendererAccessor) mc.levelRenderer).stellaris$getSkyBuffer().drawWithShader(matrix4f, projectionMatrix, shaderInstance);
        VertexBuffer.unbind();
    }

    public static void drawStars(VertexBuffer vertexBuffer, PoseStack poseStack, Matrix4f projectionMatrix, ShaderInstance shaderInstance, Camera camera, float dayAngle, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        Matrix4f matrix4f = setMatrixRot(poseStack, Triple.of(Axis.YP.rotationDegrees(camera.getYRot() - 90), Axis.XP.rotationDegrees(dayAngle), Axis.ZP.rotationDegrees(0)));

        FogRenderer.setupNoFog();
        vertexBuffer.bind();
        vertexBuffer.drawWithShader(matrix4f, projectionMatrix, shaderInstance);
        VertexBuffer.unbind();

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void drawMoonWithPhase(BufferBuilder bufferBuilder, PoseStack poseStack, Camera camera, CustomVanillaObject customVanillaObject, float dayAngle) {
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

        Matrix4f matrix4f = setMatrixRot(poseStack, Triple.of(Axis.YP.rotationDegrees(camera.getYRot() - 90), Axis.XP.rotationDegrees(dayAngle), Axis.ZP.rotationDegrees(camera.getXRot())));

        RenderSystem.setShaderTexture(0, customVanillaObject.moonTexture());
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, -16.0F, -100.0F, 16.0F).uv(endX, endY).endVertex();
        bufferBuilder.vertex(matrix4f, 16.0F, -100.0F, 16.0F).uv(startX, endY).endVertex();
        bufferBuilder.vertex(matrix4f, 16.0F, -100.0F, -16.0F).uv(startX, startY).endVertex();
        bufferBuilder.vertex(matrix4f, -16.0F, -100.0F, -16.0F).uv(endX, startY).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    public static void drawCelestialBody(SkyObject skyObject, BufferBuilder bufferBuilder, PoseStack poseStack, Camera camera, float dayAngle, boolean blend) {
        drawCelestialBody(skyObject.texture(), bufferBuilder, poseStack, camera, skyObject.size(), dayAngle, blend);
    }

    public static void drawCelestialBody(ResourceLocation texture, BufferBuilder bufferBuilder, PoseStack poseStack, Camera camera, float size, float dayAngle, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        Matrix4f matrix4f = setMatrixRot(poseStack, Triple.of(Axis.YP.rotationDegrees(camera.getYRot() - 90), Axis.XP.rotationDegrees(dayAngle), Axis.ZP.rotationDegrees(camera.getXRot())));

        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, texture);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, -size, 100, -size).uv(1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, size, 100, -size).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, size, 100, size).uv(0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(matrix4f, -size, 100, size).uv(1.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());

        if (blend) {
            RenderSystem.disableBlend();
        }
    }
}