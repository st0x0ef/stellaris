package com.st0x0ef.stellaris.client.skys.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.st0x0ef.stellaris.mixin.client.LevelRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class SkyHelper {
    public static void drawStars(VertexBuffer vertexBuffer, Matrix4f matrix4f, Matrix4f projectionMatrix, ShaderInstance shaderInstance, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        FogRenderer.setupNoFog();
        vertexBuffer.bind();
        vertexBuffer.drawWithShader(matrix4f, projectionMatrix, shaderInstance);
        VertexBuffer.unbind();

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void setupShaderColor(Minecraft mc, float r, float g, float b) {
        if (mc.level.effects().hasGround()) {
            RenderSystem.setShaderColor(r * 0.2F + 0.04F, g * 0.2F + 0.04F, b * 0.6F + 0.1F, 1.0F);
        } else {
            RenderSystem.setShaderColor(r, g, b, 1.0F);
        }
    }

    public static void drawSky(Minecraft mc, Matrix4f matrix4f, Matrix4f projectionMatrix, ShaderInstance shaderInstance) {
        ((LevelRendererAccessor) mc.levelRenderer).stellaris$getSkyBuffer().bind();
        ((LevelRendererAccessor) mc.levelRenderer).stellaris$getSkyBuffer().drawWithShader(matrix4f, projectionMatrix, shaderInstance);
        VertexBuffer.unbind();
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

    public static void drawCelestialBody(ResourceLocation texture, Vec3 color, BufferBuilder bufferBuilder, PoseStack poseStack, Matrix4f matrix4f, float size, float y, float dayAngle, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        int r = (int) color.x();
        int g = (int) color.y();
        int b = (int) color.z();
        int a = 255;

        Matrix4f matrix4f2 = setMatrixRot(poseStack, Triple.of(Axis.YP.rotationDegrees(-90), Axis.XP.rotationDegrees(dayAngle), null));

        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, texture);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferBuilder.vertex(matrix4f2, -size, y, -size).color(r, g, b, a).uv(1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f2, size, y, -size).color(r, g, b, a).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f2, size, y, size).color(r, g, b, a).uv(0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(matrix4f2, -size, y, size).color(r, g, b, a).uv(1.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());

        if (blend) {
            RenderSystem.disableBlend();
        }
    }
}